package vn.com.routex.hub.booking.service.infrastructure.persistence.adapter.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.domain.payment.PaymentStatus;
import vn.com.routex.hub.booking.service.domain.payment.model.Payment;
import vn.com.routex.hub.booking.service.domain.payment.port.PaymentRepositoryPort;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.payment.repository.PaymentEntityRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PaymentRepositoryAdapter implements PaymentRepositoryPort {

    private final PaymentEntityRepository paymentJpaRepository;
    private final PaymentPersistenceMapper paymentPersistenceMapper;

    @Override
    public Payment save(Payment payment) {
        return paymentPersistenceMapper.toDomain(
                paymentJpaRepository.save(paymentPersistenceMapper.toJpaEntity(payment))
        );
    }

    @Override
    public Optional<Payment> findById(String id) {
        return paymentJpaRepository.findById(id).map(paymentPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Payment> findByBookingIdAndStatus(String bookingId, PaymentStatus status) {
        return paymentJpaRepository.findByBookingIdAndStatus(bookingId, status).map(paymentPersistenceMapper::toDomain);
    }
}
