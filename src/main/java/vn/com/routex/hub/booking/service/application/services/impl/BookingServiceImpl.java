package vn.com.routex.hub.booking.service.application.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.hub.booking.service.application.command.booking.CreateBookingCommand;
import vn.com.routex.hub.booking.service.application.services.BookingService;
import vn.com.routex.hub.booking.service.domain.booking.BookingSeatStatus;
import vn.com.routex.hub.booking.service.domain.booking.BookingStatus;
import vn.com.routex.hub.booking.service.domain.booking.model.Booking;
import vn.com.routex.hub.booking.service.domain.booking.model.BookingSeat;
import vn.com.routex.hub.booking.service.domain.booking.port.BookingRepositoryPort;
import vn.com.routex.hub.booking.service.domain.booking.port.BookingSeatRepositoryPort;
import vn.com.routex.hub.booking.service.domain.route.model.RouteAssignmentRecord;
import vn.com.routex.hub.booking.service.domain.route.port.RouteAssignmentRepositoryPort;
import vn.com.routex.hub.booking.service.domain.seat.model.RouteSeat;
import vn.com.routex.hub.booking.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.booking.service.infrastructure.persistence.utils.ExceptionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.ASSIGNMENT_NOT_FOUND;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepositoryPort bookingRepositoryPort;
    private final BookingSeatRepositoryPort bookingSeatRepositoryPort;
    private final RouteAssignmentRepositoryPort routeAssignmentRepositoryPort;
    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @Override
    @Transactional
    public Booking createBooking(CreateBookingCommand command, List<RouteSeat> routeSeats) {
        sLog.info("[BOOK-SERVICE] Create Draft Booking Command: {}", command);


        RouteAssignmentRecord assignmentRecord = routeAssignmentRepositoryPort.findActiveByRouteId(command.routeId())
                .orElseThrow(() -> new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(ASSIGNMENT_NOT_FOUND, command.routeId()))));

        BigDecimal basePrice = assignmentRecord.getTicketPrice();
        BigDecimal totalAmount = basePrice.multiply(BigDecimal.valueOf(routeSeats.size()));

        Booking booking = Booking.builder()
                .id(UUID.randomUUID().toString())
                .bookingCode(bookingRepositoryPort.generateBookingCode())
                .routeId(command.routeId())
                .merchantId(command.merchantId())
                .vehicleId(assignmentRecord.getVehicleId())
                .customerId(command.customerId())
                .customerName(command.customerName())
                .customerPhone(command.customerPhone())
                .customerEmail(command.customerEmail())
                .channel(command.context().channel())
                .seatCount(routeSeats.size())
                .totalAmount(totalAmount)
                .currency("VND")
                .status(BookingStatus.PENDING_PAYMENT)
                .heldAt(command.heldAt())
                .holdUntil(command.holdUntil())
                .creator(command.holdBy())
                .build();

        List<BookingSeat> bookingSeats = routeSeats.stream()
                .map(seat -> BookingSeat.builder()
                        .id(UUID.randomUUID().toString())
                        .bookingId(booking.getId())
                        .routeId(command.routeId())
                        .seatNo(seat.getSeatNo())
                        .creator(command.holdBy())
                        .status(BookingSeatStatus.HELD)
                        .price(basePrice)
                        .build())
                .toList();

        bookingRepositoryPort.save(booking);
        bookingSeatRepositoryPort.saveAll(bookingSeats);

        sLog.info("[BOOK-SERVICE] Create Draft Booking successfully: {}", booking);
        return booking;
    }
}
