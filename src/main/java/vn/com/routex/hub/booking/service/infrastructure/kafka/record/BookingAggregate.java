package vn.com.routex.hub.booking.service.infrastructure.kafka.record;


import vn.com.routex.hub.booking.service.domain.booking.model.Booking;
import vn.com.routex.hub.booking.service.domain.booking.model.BookingSeat;
import vn.com.routex.hub.booking.service.domain.payment.model.Payment;
import vn.com.routex.hub.booking.service.domain.seat.model.RouteSeat;

import java.util.List;

public record BookingAggregate(
        Payment payment,
        Booking booking,
        List<BookingSeat> bookingSeats,
        List<RouteSeat> routeSeats
) {
}
