package vn.com.routex.hub.booking.service.domain.booking.port;

import vn.com.routex.hub.booking.service.domain.booking.model.Booking;

import java.util.Optional;

public interface BookingRepositoryPort {
    Booking save(Booking booking);

    Optional<Booking> findById(String bookingId);

    String generateBookingCode();
}
