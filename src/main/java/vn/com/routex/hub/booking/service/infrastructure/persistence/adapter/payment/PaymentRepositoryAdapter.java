package vn.com.routex.hub.booking.service.infrastructure.persistence.adapter.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.domain.payment.model.Payment;
import vn.com.routex.hub.booking.service.domain.payment.port.PaymentRepositoryPort;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.payment.repository.PaymentEntityRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PaymentRepositoryAdapter implements PaymentRepositoryPort {

    private final PaymentEntityRepository paymentEntityRepository;
    private final PaymentPersistenceMapper paymentPersistenceMapper;

    @Override
    public Payment save(Payment payment) {
        return paymentPersistenceMapper.toDomain(
                paymentEntityRepository.save(paymentPersistenceMapper.toEntity(payment))
        );
    }

    @Override
    public Optional<Payment> findById(String id) {
        return paymentEntityRepository.findById(id).map(paymentPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Payment> findByBookingCode(String bookingCode) {
        return paymentEntityRepository.findByBookingCode(bookingCode)
                .map(paymentPersistenceMapper::toDomain);
    }
}
