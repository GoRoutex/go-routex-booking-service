package vn.com.routex.hub.booking.service.infrastructure.kafka.record;


import vn.com.routex.hub.booking.service.domain.booking.model.Booking;
import vn.com.routex.hub.booking.service.domain.booking.model.BookingSeat;
import vn.com.routex.hub.booking.service.domain.seat.model.TripSeat;

import java.util.List;

public record BookingAggregate(
        Booking booking,
        List<BookingSeat> bookingSeats,
        List<TripSeat> tripSeats
) {
}
