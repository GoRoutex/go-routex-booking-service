package vn.com.routex.hub.booking.service.application.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.booking.service.application.services.BookingService;
import vn.com.routex.hub.booking.service.application.services.FareConfigService;
import vn.com.routex.hub.booking.service.controller.models.booking.CreateBookingRequest;
import vn.com.routex.hub.booking.service.domain.booking.Booking;
import vn.com.routex.hub.booking.service.domain.booking.BookingRepository;
import vn.com.routex.hub.booking.service.domain.booking.BookingSeat;
import vn.com.routex.hub.booking.service.domain.booking.BookingSeatRepository;
import vn.com.routex.hub.booking.service.domain.booking.BookingSeatStatus;
import vn.com.routex.hub.booking.service.domain.booking.BookingStatus;
import vn.com.routex.hub.booking.service.domain.seat.RouteSeat;
import vn.com.routex.hub.booking.service.domain.vehicle.Vehicle;
import vn.com.routex.hub.booking.service.domain.vehicle.VehicleRepository;
import vn.com.routex.hub.booking.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.booking.service.infrastructure.persistence.log.SystemLog;
import vn.com.routex.hub.booking.service.infrastructure.persistence.utils.ExceptionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.VEHICLE_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final FareConfigService fareConfigService;
    private final VehicleRepository vehicleRepository;
    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @Override
    public Booking createBooking(CreateBookingRequest request, List<RouteSeat> routeSeats) {

        String routeId = request.getData().getRouteId();

        sLog.info("[BOOK-SERVICE] Create Draft Booking Request: {}", request);

        CreateBookingRequest.CreateBookingRequestInformation info = request.getInfo();

        Vehicle vehicle = vehicleRepository.findById(request.getData().getVehicleId())
                .orElseThrow(() -> new BusinessException(request.getRequestId(), request.getRequestDateTime(), request.getChannel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, VEHICLE_NOT_FOUND)));

        BigDecimal basePrice = fareConfigService.getUnitPrice(vehicle.getType());

        BigDecimal totalAmount = basePrice.multiply(BigDecimal.valueOf(routeSeats.size()));

        Booking booking = Booking
                .builder()
                .id(UUID.randomUUID().toString())
                .bookingCode(bookingRepository.generateRouteCode())
                .routeId(routeId)
                .customerId(info.getCustomerId())
                .seatCount(routeSeats.size())
                .totalAmount(totalAmount)
                .currency(info.getCurrency())
                .status(BookingStatus.PENDING_PAYMENT)
                .heldAt(request.getData().getHeldAt())
                .holdUntil(request.getData().getHoldUntil())
                .creator(request.getData().getHoldBy())
                .build();


        List<BookingSeat> bookingSeats = routeSeats.stream()
                .map(seat -> BookingSeat.builder()
                        .id(UUID.randomUUID().toString())
                        .bookingId(booking.getId())
                        .routeId(routeId)
                        .seatNo(seat.getSeatNo())
                        .creator(request.getData().getHoldBy())
                        .status(BookingSeatStatus.HELD)
                        .price(basePrice)
                        .build())
                .collect(Collectors.toList());

        sLog.info("[BOOK-SERVICE] Create Draft Booking successfully: {}", booking);

        bookingRepository.save(booking);
        bookingSeatRepository.saveAll(bookingSeats);

        return booking;
    }
}
