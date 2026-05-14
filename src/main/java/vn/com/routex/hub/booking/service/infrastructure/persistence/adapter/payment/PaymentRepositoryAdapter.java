package vn.com.routex.hub.booking.service.infrastructure.persistence.adapter.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.domain.booking.PaymentStatus;
import vn.com.routex.hub.booking.service.domain.payment.PaymentMethod;
import vn.com.routex.hub.booking.service.domain.payment.model.PaymentAggregate;
import vn.com.routex.hub.booking.service.domain.payment.port.PaymentRepositoryPort;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.payment.repository.PaymentEntityRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PaymentRepositoryAdapter implements PaymentRepositoryPort {

    private final PaymentEntityRepository paymentEntityRepository;
    private final PaymentPersistenceMapper paymentPersistenceMapper;

    @Override
    public Optional<PaymentAggregate> findById(String paymentId) {
        return paymentEntityRepository.findById(paymentId).map(paymentPersistenceMapper::toDomain);
    }

    @Override
    public Optional<PaymentAggregate> findByBookingCodeAndMethodAndStatus(String bookingId, PaymentMethod method, PaymentStatus status) {
        return paymentEntityRepository.findByBookingCodeAndMethodAndStatus(bookingId, method, status)
                .map(paymentPersistenceMapper::toDomain);
    }

    @Override
    public PaymentAggregate save(PaymentAggregate paymentAggregate) {
        return paymentPersistenceMapper.toDomain(
                paymentEntityRepository.save(paymentPersistenceMapper.toEntity(paymentAggregate))
        );
    }

    @Override
    public Optional<PaymentAggregate> findByTxnRef(String txnRef) {
        return paymentEntityRepository.findByTxnRef(txnRef)
                .map(paymentPersistenceMapper::toDomain);
    }

    @Override
    public Optional<PaymentAggregate> findByBookingCode(String bookingCode) {
        return paymentEntityRepository.findByBookingCode(bookingCode)
                .map(paymentPersistenceMapper::toDomain);
    }
}
