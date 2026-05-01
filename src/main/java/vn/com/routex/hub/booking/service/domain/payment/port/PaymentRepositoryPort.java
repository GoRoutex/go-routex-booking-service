package vn.com.routex.hub.booking.service.domain.payment.port;

import vn.com.routex.hub.booking.service.domain.payment.PaymentStatus;
import vn.com.routex.hub.booking.service.domain.payment.model.Payment;

import java.util.Optional;

/**
 * Domain repository port (no Spring Data/JPA dependency).
 */
public interface PaymentRepositoryPort {

    Payment save(Payment payment);

    Optional<Payment> findById(String id);

    Optional<Payment> findByBookingCode(String bookingCode);
}

