package vn.com.routex.hub.booking.service.domain.booking.port;


import vn.com.routex.hub.booking.service.domain.booking.model.Booking;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepositoryPort {

    Optional<Booking> findById(String bookingId);

    Optional<Booking> findByBookingCodeForUpdate(String bookingId);

    Optional<Booking> findByBookingCode(String bookingCode);

    Optional<Booking> findById(String bookingId, String merchantId);

    List<Booking> findExpiredPendingPaymentBookingsForUpdate(OffsetDateTime holdUntil, int limit);

    Booking save(Booking booking);

    String generateBookingCode();
}
