package vn.com.routex.hub.booking.service.application.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.hub.booking.service.application.command.booking.CreateBookingCommand;
import vn.com.routex.hub.booking.service.application.command.seat.HoldSeatCommand;
import vn.com.routex.hub.booking.service.application.command.seat.HoldSeatResult;
import vn.com.routex.hub.booking.service.application.services.BookingService;
import vn.com.routex.hub.booking.service.application.services.HoldSeatService;
import vn.com.routex.hub.booking.service.domain.booking.model.Booking;
import vn.com.routex.hub.booking.service.domain.route.model.RouteAggregate;
import vn.com.routex.hub.booking.service.domain.route.port.RouteAggregateRepositoryPort;
import vn.com.routex.hub.booking.service.domain.seat.SeatStatus;
import vn.com.routex.hub.booking.service.domain.seat.model.TripSeat;
import vn.com.routex.hub.booking.service.domain.seat.port.TripSeatRepositoryPort;
import vn.com.routex.hub.booking.service.infrastructure.cache.redis.models.TripCacheSeat;
import vn.com.routex.hub.booking.service.infrastructure.cache.redis.service.TripSeatCacheService;
import vn.com.routex.hub.booking.service.infrastructure.cache.redisson.RedisDistributedLocker;
import vn.com.routex.hub.booking.service.infrastructure.cache.redisson.RedisDistributedService;
import vn.com.routex.hub.booking.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.booking.service.infrastructure.persistence.utils.ExceptionUtils;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.INVALID_DATA_ERROR;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.INVALID_INPUT_ERROR;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.INVALID_SEAT_NO;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.SEAT_NOT_AVAILABLE;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.SEAT_NOT_FOUND;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.SYSTEM_ERROR;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.SYSTEM_ERROR_MESSAGE;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.TRIP_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class HoldSeatServiceImpl implements HoldSeatService {

    private final TripSeatRepositoryPort tripSeatRepositoryPort;
    private final RouteAggregateRepositoryPort routeAggregateRepositoryPort;
    private final RedisDistributedService redisDistributedService;
    private final TripSeatCacheService tripSeatCacheService;
    private final BookingService bookingService;


    private static final String LOCK_PATTERN = "lock:seat:";
    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @Override
    @Transactional
    public HoldSeatResult holdSeat(HoldSeatCommand command) {

        String holdToken = UUID.randomUUID().toString();
        sLog.info("[BOOK-SERVICE] Hold Seat Command: {}", command);
        List<String> distinctSeatNos = validateAndNormalizeSeat(command);
        RouteAggregate route = validateRoute(command);
        return executeWithSeatLocks(command, distinctSeatNos, () -> {
            Map<String, TripCacheSeat> mapCacheSeats = tripSeatCacheService.getSpecificSeat(command.tripId(), distinctSeatNos);
            if(!mapCacheSeats.isEmpty()) {
                for(String seatNo : distinctSeatNos) {
                    TripCacheSeat cacheSeat = mapCacheSeats.get(seatNo);
                    if(cacheSeat != null && !SeatStatus.AVAILABLE.equals(cacheSeat.status())) {
                        throw new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                                ExceptionUtils.buildResultResponse(INVALID_DATA_ERROR, String.format(SEAT_NOT_AVAILABLE, seatNo)));
                    }
                }
            }

            List<TripSeat> tripSeats = getAndValidateRouteSeats(command, distinctSeatNos);
            OffsetDateTime now = OffsetDateTime.now();
            OffsetDateTime holdUntil = now.plusMinutes(5);
            tripSeats.forEach(seat -> seat.setStatus(SeatStatus.HELD));
            tripSeatRepositoryPort.saveAll(tripSeats);
            updateSeatCache(command.tripId(), tripSeats);
            Booking booking = createBooking(command, route, holdToken, now, holdUntil, tripSeats);

            return HoldSeatResult.builder()
                    .booking(HoldSeatResult.HoldSeatBookingResult.builder()
                            .bookingId(booking.getId())
                            .bookingCode(booking.getBookingCode())
                            .holdUntil(booking.getHoldUntil())
                            .seatCount(booking.getSeatCount())
                            .totalAmount(booking.getTotalAmount())
                            .currency(booking.getCurrency())
                            .build())
                    .seats(tripSeats.stream()
                            .sorted(Comparator.comparing(TripSeat::getSeatNo))
                            .map(seat -> HoldSeatResult.HoldSeatItemResult.builder()
                                    .tripId(seat.getTripId())
                                    .seatNo(seat.getSeatNo())
                                    .status(seat.getStatus().name())
                                    .holdToken(holdToken)
                                    .build())
                            .toList())
                    .build();
        });
    }

    private void updateSeatCache(String tripId, List<TripSeat> tripSeats) {
        List<TripCacheSeat> updates = tripSeats
                .stream()
                .map(seat -> TripCacheSeat.builder()
                        .tripId(seat.getTripId())
                        .seatNo(seat.getSeatNo())
                        .status(seat.getStatus())
                        .build()).toList();

        tripSeatCacheService.updateSeatsStatus(tripId, updates);
    }

    private Booking createBooking(HoldSeatCommand command, RouteAggregate route, String holdToken, OffsetDateTime heldAt, OffsetDateTime holdUntil, List<TripSeat> tripSeats) {
        return bookingService.createBooking(CreateBookingCommand.builder()
                .context(command.context())
                .merchantId(route.getMerchantId())
                .tripId(command.tripId())
                .holdBy(command.holdBy())
                .holdToken(holdToken)
                .heldAt(heldAt)
                .holdUntil(holdUntil)
                .customerName(command.customerName())
                .customerPhone(command.customerPhone())
                .customerEmail(command.customerEmail())
                .build(), tripSeats);
    }

    private List<TripSeat> getAndValidateRouteSeats(HoldSeatCommand command, List<String> distinctSeatNos) {
        List<TripSeat> tripSeats = tripSeatRepositoryPort.findAllByTripIdAndSeatNoInForUpdate(command.tripId(), distinctSeatNos);
        if (tripSeats.size() != distinctSeatNos.size()) {
            throw new BusinessException(
                    command.context().requestId(),
                    command.context().requestDateTime(),
                    command.context().channel(),
                    ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, SEAT_NOT_FOUND)
            );
        }

        for (TripSeat seat : tripSeats) {
            if (!SeatStatus.AVAILABLE.equals(seat.getStatus())) {
                throw new BusinessException(
                        command.context().requestId(),
                        command.context().requestDateTime(),
                        command.context().channel(),
                        ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, String.format(SEAT_NOT_AVAILABLE, seat.getSeatNo()))
                );
            }
        }
        return tripSeats;
    }
    private HoldSeatResult executeWithSeatLocks(
            HoldSeatCommand command,
            List<String> seatNos,
            Supplier<HoldSeatResult> action
    ) {

        List<String> lockKeys = seatNos
                .stream()
                .map(seatNo -> LOCK_PATTERN + seatNo)
                .toList();

        RedisDistributedLocker multiLock = redisDistributedService.getMultiLock(lockKeys);

        try {
            if(!multiLock.tryLock(5, 10, TimeUnit.SECONDS)) {
                throw new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                        ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, "Specific seat is processed by other user"));
            }

            try {
                return action.get();
            } finally {
                if(multiLock.isHeldByCurrentThread()) {
                    multiLock.unlock();
                }
            }
        } catch(InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                    ExceptionUtils.buildResultResponse(SYSTEM_ERROR, SYSTEM_ERROR_MESSAGE));
        }
    }
    private List<String> validateAndNormalizeSeat(HoldSeatCommand command) {
        if (command.seatNos() == null || command.seatNos().isEmpty()) {
            throw new BusinessException(
                    command.context().requestId(),
                    command.context().requestDateTime(),
                    command.context().channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_SEAT_NO)
            );
        }

        List<String> distinctSeatNos = command.seatNos().stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .map(String::toUpperCase)
                .filter(seatNo -> !seatNo.isEmpty())
                .distinct()
                .sorted()
                .toList();

        if (distinctSeatNos.isEmpty()) {
            throw new BusinessException(
                    command.context().requestId(),
                    command.context().requestDateTime(),
                    command.context().channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_SEAT_NO)
            );
        }

        return distinctSeatNos;
    }

    private RouteAggregate validateRoute(HoldSeatCommand command) {
        return routeAggregateRepositoryPort.findById(command.tripId())
                .orElseThrow(() -> new BusinessException(
                        command.context().requestId(),
                        command.context().requestDateTime(),
                        command.context().channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(TRIP_NOT_FOUND, command.tripId()))
                ));
    }
}
