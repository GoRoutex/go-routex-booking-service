package vn.com.routex.hub.booking.service.infrastructure.persistence.adapter.payment;

import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.domain.payment.model.Payment;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.payment.entity.PaymentEntity;

@Component
public class PaymentPersistenceMapper {

    public Payment toDomain(PaymentEntity entity) {
        return Payment.builder()
                .id(entity.getId())
                .bookingCode(entity.getBookingCode())
                .method(entity.getMethod())
                .txnRef(entity.getTxnRef())
                .amount(entity.getAmount())
                .currency(entity.getCurrency())
                .status(entity.getStatus())
                .paidAt(entity.getPaidAt())
                .failedAt(entity.getFailedAt())
                .failureReason(entity.getFailureReason())
                .description(entity.getDescription())
                .build();
    }

    public PaymentEntity toEntity(Payment payment) {
        return PaymentEntity.builder()
                .id(payment.getId())
                .bookingCode(payment.getBookingCode())
                .txnRef(payment.getTxnRef())
                .method(payment.getMethod())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .status(payment.getStatus())
                .paidAt(payment.getPaidAt())
                .failedAt(payment.getFailedAt())
                .failureReason(payment.getFailureReason())
                .description(payment.getDescription())
                .build();
    }
}
