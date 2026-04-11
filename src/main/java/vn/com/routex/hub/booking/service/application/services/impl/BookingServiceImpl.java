package vn.com.routex.hub.booking.service.application.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.booking.service.application.dto.booking.CreateBookingCommand;
import vn.com.routex.hub.booking.service.application.services.BookingService;
import vn.com.routex.hub.booking.service.application.services.FareConfigService;
import vn.com.routex.hub.booking.service.domain.booking.BookingSeatStatus;
import vn.com.routex.hub.booking.service.domain.booking.BookingStatus;
import vn.com.routex.hub.booking.service.domain.booking.model.Booking;
import vn.com.routex.hub.booking.service.domain.booking.model.BookingSeat;
import vn.com.routex.hub.booking.service.domain.booking.port.BookingRepositoryPort;
import vn.com.routex.hub.booking.service.domain.booking.port.BookingSeatRepositoryPort;
import vn.com.routex.hub.booking.service.domain.seat.model.RouteSeat;
import vn.com.routex.hub.booking.service.domain.vehicle.model.VehicleProfile;
import vn.com.routex.hub.booking.service.domain.vehicle.port.VehicleRepositoryPort;
import vn.com.routex.hub.booking.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.booking.service.infrastructure.persistence.log.SystemLog;
import vn.com.routex.hub.booking.service.infrastructure.persistence.utils.ExceptionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.VEHICLE_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final FareConfigService fareConfigService;
    private final VehicleRepositoryPort vehicleRepositoryPort;
    private final BookingRepositoryPort bookingRepositoryPort;
    private final BookingSeatRepositoryPort bookingSeatRepositoryPort;
    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @Override
    public Booking createBooking(CreateBookingCommand command, List<RouteSeat> routeSeats) {
        sLog.info("[BOOK-SERVICE] Create Draft Booking Command: {}", command);

        VehicleProfile vehicle = vehicleRepositoryPort.findById(command.vehicleId(), command.merchantId())
                .orElseThrow(() -> new BusinessException(
                        command.metadata().requestId(),
                        command.metadata().requestDateTime(),
                        command.metadata().channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, VEHICLE_NOT_FOUND)
                ));

        BigDecimal basePrice = fareConfigService.getUnitPrice(vehicle.getType());
        BigDecimal totalAmount = basePrice.multiply(BigDecimal.valueOf(routeSeats.size()));

        Booking booking = Booking.builder()
                .id(UUID.randomUUID().toString())
                .bookingCode(bookingRepositoryPort.generateBookingCode())
                .routeId(command.routeId())
                .merchantId(command.merchantId())
                .vehicleId(command.vehicleId())
                .customerId(command.customerId())
                .channel(command.metadata().channel())
                .seatCount(routeSeats.size())
                .totalAmount(totalAmount)
                .currency(command.currency())
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
