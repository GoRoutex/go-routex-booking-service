package vn.com.routex.hub.booking.service.application.services.impl;

import io.lettuce.core.BitFieldArgs;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.context.ApplicationEventPublisher;
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
import vn.com.routex.hub.booking.service.domain.seat.model.RouteSeat;
import vn.com.routex.hub.booking.service.domain.seat.port.RouteSeatRepositoryPort;
import vn.com.routex.hub.booking.service.infrastructure.cache.redis.models.RouteCacheSeat;
import vn.com.routex.hub.booking.service.infrastructure.cache.redis.service.RouteSeatCacheService;
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
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.ROUTE_NOT_FOUND;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.SEAT_NOT_AVAILABLE;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.SEAT_NOT_FOUND;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.SYSTEM_ERROR;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.SYSTEM_ERROR_MESSAGE;

@Service
@RequiredArgsConstructor
public class HoldSeatServiceImpl implements HoldSeatService {

    private final RouteSeatRepositoryPort routeSeatRepositoryPort;
    private final RouteAggregateRepositoryPort routeAggregateRepositoryPort;
    private final RedisDistributedService redisDistributedService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final RedissonClient redissonClient;
    private final RouteSeatCacheService routeSeatCacheService;
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
            Map<String, RouteCacheSeat> mapCacheSeats = routeSeatCacheService.getSpecificSeat(command.routeId(), distinctSeatNos);
            if(!mapCacheSeats.isEmpty()) {
                for(String seatNo : distinctSeatNos) {
                    RouteCacheSeat cacheSeat = mapCacheSeats.get(seatNo);
                    if(cacheSeat != null && !SeatStatus.AVAILABLE.equals(cacheSeat.status())) {
                        throw new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                                ExceptionUtils.buildResultResponse(INVALID_DATA_ERROR, String.format(SEAT_NOT_AVAILABLE, seatNo)));
                    }
                }
            }

            List<RouteSeat> routeSeats = getAndValidateRouteSeats(command, distinctSeatNos);
            OffsetDateTime now = OffsetDateTime.now();
            OffsetDateTime holdUntil = now.plusMinutes(5);
            routeSeats.forEach(seat -> seat.setStatus(SeatStatus.HELD));
            routeSeatRepositoryPort.saveAll(routeSeats);
            updateSeatCache(command.routeId(), routeSeats);
            Booking booking = createBooking(command, route, holdToken, now, holdUntil, routeSeats);

            return HoldSeatResult.builder()
                    .booking(HoldSeatResult.HoldSeatBookingResult.builder()
                            .bookingId(booking.getId())
                            .bookingCode(booking.getBookingCode())
                            .holdUntil(booking.getHoldUntil())
                            .seatCount(booking.getSeatCount())
                            .totalAmount(booking.getTotalAmount())
                            .currency(booking.getCurrency())
                            .build())
                    .seats(routeSeats.stream()
                            .sorted(Comparator.comparing(RouteSeat::getSeatNo))
                            .map(seat -> HoldSeatResult.HoldSeatItemResult.builder()
                                    .routeId(seat.getRouteId())
                                    .seatNo(seat.getSeatNo())
                                    .status(seat.getStatus().name())
                                    .holdToken(holdToken)
                                    .build())
                            .toList())
                    .build();
        });
    }

    private void updateSeatCache(String routeId, List<RouteSeat> routeSeats) {
        List<RouteCacheSeat> updates = routeSeats
                .stream()
                .map(seat -> RouteCacheSeat.builder()
                        .routeId(seat.getRouteId())
                        .seatNo(seat.getSeatNo())
                        .status(seat.getStatus())
                        .build()).toList();

        routeSeatCacheService.updateSeatsStatus(routeId, updates);
    }

    private Booking createBooking(HoldSeatCommand command, RouteAggregate route, String holdToken, OffsetDateTime heldAt, OffsetDateTime holdUntil, List<RouteSeat> routeSeats) {
        return bookingService.createBooking(CreateBookingCommand.builder()
                .context(command.context())
                .merchantId(route.getMerchantId())
                .routeId(command.routeId())
                .holdBy(command.holdBy())
                .holdToken(holdToken)
                .heldAt(heldAt)
                .holdUntil(holdUntil)
                .customerName(command.customerName())
                .customerPhone(command.customerPhone())
                .customerEmail(command.customerEmail())
                .build(), routeSeats);
    }

    private List<RouteSeat> getAndValidateRouteSeats(HoldSeatCommand command, List<String> distinctSeatNos) {
        List<RouteSeat> routeSeats = routeSeatRepositoryPort.findAllByRouteIdAndSeatNoInForUpdate(command.routeId(), distinctSeatNos);
        if (routeSeats.size() != distinctSeatNos.size()) {
            throw new BusinessException(
                    command.context().requestId(),
                    command.context().requestDateTime(),
                    command.context().channel(),
                    ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, SEAT_NOT_FOUND)
            );
        }

        for (RouteSeat seat : routeSeats) {
            if (!SeatStatus.AVAILABLE.equals(seat.getStatus())) {
                throw new BusinessException(
                        command.context().requestId(),
                        command.context().requestDateTime(),
                        command.context().channel(),
                        ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, String.format(SEAT_NOT_AVAILABLE, seat.getSeatNo()))
                );
            }
        }
        return routeSeats;
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
        return routeAggregateRepositoryPort.findById(command.routeId())
                .orElseThrow(() -> new BusinessException(
                        command.context().requestId(),
                        command.context().requestDateTime(),
                        command.context().channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(ROUTE_NOT_FOUND, command.routeId()))
                ));
    }
}
