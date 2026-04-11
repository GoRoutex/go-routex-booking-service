package vn.com.routex.hub.booking.service.application.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.booking.service.application.dto.booking.CreateBookingCommand;
import vn.com.routex.hub.booking.service.application.dto.seat.HoldSeatCommand;
import vn.com.routex.hub.booking.service.application.dto.seat.HoldSeatResult;
import vn.com.routex.hub.booking.service.application.services.BookingService;
import vn.com.routex.hub.booking.service.application.services.HoldSeatService;
import vn.com.routex.hub.booking.service.domain.assignment.RouteAssignmentStatus;
import vn.com.routex.hub.booking.service.domain.booking.model.Booking;
import vn.com.routex.hub.booking.service.domain.route.model.RouteAggregate;
import vn.com.routex.hub.booking.service.domain.route.model.RouteAssignmentRecord;
import vn.com.routex.hub.booking.service.domain.route.port.RouteAggregateRepositoryPort;
import vn.com.routex.hub.booking.service.domain.route.port.RouteAssignmentRepositoryPort;
import vn.com.routex.hub.booking.service.domain.seat.SeatStatus;
import vn.com.routex.hub.booking.service.domain.seat.model.RouteSeat;
import vn.com.routex.hub.booking.service.domain.seat.port.RouteSeatRepositoryPort;
import vn.com.routex.hub.booking.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.booking.service.infrastructure.persistence.log.SystemLog;
import vn.com.routex.hub.booking.service.infrastructure.persistence.utils.ExceptionUtils;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.INVALID_INPUT_ERROR;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.INVALID_SEAT_NO;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.ROUTE_NOT_FOUND;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.SEAT_NOT_AVAILABLE;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.SEAT_NOT_FOUND;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.VEHICLE_NOT_ASSIGNED_TO_ROUTE;

@Service
@RequiredArgsConstructor
public class HoldSeatServiceImpl implements HoldSeatService {

    private final RouteSeatRepositoryPort routeSeatRepositoryPort;
    private final RouteAggregateRepositoryPort routeAggregateRepositoryPort;
    private final RouteAssignmentRepositoryPort routeAssignmentRepositoryPort;
    private final BookingService bookingService;
    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @Override
    @Transactional
    public HoldSeatResult holdSeat(HoldSeatCommand command) {
        String holdToken = UUID.randomUUID().toString();
        sLog.info("[BOOK-SERVICE] Hold Seat Command: {}", command);

        if (command.seatNos() == null || command.seatNos().isEmpty()) {
            throw new BusinessException(
                    command.metadata().requestId(),
                    command.metadata().requestDateTime(),
                    command.metadata().channel(),
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
                    command.metadata().requestId(),
                    command.metadata().requestDateTime(),
                    command.metadata().channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_SEAT_NO)
            );
        }

        RouteAggregate route = validateRoute(command);
        validateAssignedVehicle(command, route.getMerchantId());

        List<RouteSeat> routeSeats = routeSeatRepositoryPort.findAllByRouteIdAndSeatNoInForUpdate(command.routeId(), distinctSeatNos);
        if (routeSeats.size() != distinctSeatNos.size()) {
            throw new BusinessException(
                    command.metadata().requestId(),
                    command.metadata().requestDateTime(),
                    command.metadata().channel(),
                    ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, SEAT_NOT_FOUND)
            );
        }

        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime holdUntil = now.plusMinutes(5);

        for (RouteSeat seat : routeSeats) {
            if (SeatStatus.HELD.equals(seat.getStatus())) {
                seat.setStatus(SeatStatus.AVAILABLE);
            }

            if (!SeatStatus.AVAILABLE.equals(seat.getStatus())) {
                throw new BusinessException(
                        command.metadata().requestId(),
                        command.metadata().requestDateTime(),
                        command.metadata().channel(),
                        ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, String.format(SEAT_NOT_AVAILABLE, seat.getSeatNo()))
                );
            }
        }

        routeSeats.forEach(seat -> seat.setStatus(SeatStatus.HELD));
        routeSeatRepositoryPort.saveAll(routeSeats);

        Booking booking = bookingService.createBooking(CreateBookingCommand.builder()
                .metadata(command.metadata())
                .merchantId(route.getMerchantId())
                .routeId(command.routeId())
                .vehicleId(command.vehicleId())
                .holdBy(command.holdBy())
                .holdToken(holdToken)
                .heldAt(now)
                .holdUntil(holdUntil)
                .customerId(command.customerId())
                .customerName(command.customerName())
                .customerPhone(command.customerPhone())
                .customerEmail(command.customerEmail())
                .currency(command.currency())
                .build(), routeSeats);

        return HoldSeatResult.builder()
                .seats(routeSeats.stream()
                        .sorted(Comparator.comparing(RouteSeat::getSeatNo))
                        .map(seat -> HoldSeatResult.HoldSeatItemResult.builder()
                                .routeId(seat.getRouteId())
                                .seatNo(seat.getSeatNo())
                                .status(seat.getStatus().name())
                                .holdToken(holdToken)
                                .booking(HoldSeatResult.HoldSeatBookingResult.builder()
                                        .bookingId(booking.getId())
                                        .bookingCode(booking.getBookingCode())
                                        .holdUntil(booking.getHoldUntil())
                                        .seatCount(booking.getSeatCount())
                                        .totalAmount(booking.getTotalAmount())
                                        .currency(booking.getCurrency())
                                        .build())
                                .build())
                        .toList())
                .build();
    }

    private RouteAggregate validateRoute(HoldSeatCommand command) {
        return routeAggregateRepositoryPort.findById(command.routeId())
                .orElseThrow(() -> new BusinessException(
                        command.metadata().requestId(),
                        command.metadata().requestDateTime(),
                        command.metadata().channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(ROUTE_NOT_FOUND, command.routeId()))
                ));
    }

    private void validateAssignedVehicle(HoldSeatCommand command, String merchantId) {
        RouteAssignmentRecord assignment = routeAssignmentRepositoryPort.findActiveByRouteId(command.routeId())
                .orElseThrow(() -> new BusinessException(
                        command.metadata().requestId(),
                        command.metadata().requestDateTime(),
                        command.metadata().channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(VEHICLE_NOT_ASSIGNED_TO_ROUTE, command.routeId()))
                ));

        if (!RouteAssignmentStatus.ASSIGNED.equals(assignment.getStatus())
                || !Objects.equals(merchantId, assignment.getMerchantId())
                || !Objects.equals(command.vehicleId(), assignment.getVehicleId())) {
            throw new BusinessException(
                    command.metadata().requestId(),
                    command.metadata().requestDateTime(),
                    command.metadata().channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, String.format(VEHICLE_NOT_ASSIGNED_TO_ROUTE, command.routeId()))
            );
        }
    }
}
