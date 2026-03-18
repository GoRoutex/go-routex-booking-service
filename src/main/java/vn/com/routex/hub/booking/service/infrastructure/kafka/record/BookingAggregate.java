package vn.com.routex.hub.booking.service.infrastructure.kafka.record;


import vn.com.routex.hub.booking.service.domain.booking.Booking;
import vn.com.routex.hub.booking.service.domain.booking.BookingSeat;
import vn.com.routex.hub.booking.service.domain.seat.RouteSeat;

public record BookingAggregate(
        Booking booking,
        BookingSeat bookingSeat,
        RouteSeat routeSeat
) {
}
