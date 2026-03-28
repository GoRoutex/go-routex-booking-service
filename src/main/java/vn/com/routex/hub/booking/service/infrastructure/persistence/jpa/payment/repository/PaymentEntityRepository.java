package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.booking.service.domain.payment.PaymentStatus;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.payment.entity.PaymentEntity;

import java.util.Optional;

@Repository
public interface PaymentEntityRepository extends JpaRepository<PaymentEntity, String> {

    Optional<PaymentEntity> findByBookingIdAndStatus(String bookingId, PaymentStatus status);
}
