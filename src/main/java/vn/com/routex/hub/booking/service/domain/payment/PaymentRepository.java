package vn.com.routex.hub.booking.service.domain.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    Optional<Payment> findById(String id);
    Optional<Payment> findByBookingIdAndStatus(String bookingId, PaymentStatus status);
}
