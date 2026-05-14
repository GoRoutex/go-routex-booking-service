package vn.com.routex.hub.booking.service.infrastructure.persistence.adapter.payment;

import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.domain.payment.model.PaymentAggregate;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.payment.entity.PaymentEntity;

@Component
public class PaymentPersistenceMapper {

    public PaymentAggregate toDomain(PaymentEntity paymentJpaEntity) {
        return PaymentAggregate.builder()
                .id(paymentJpaEntity.getId())
                .bookingCode(paymentJpaEntity.getBookingCode())
                .method(paymentJpaEntity.getMethod())
                .amount(paymentJpaEntity.getAmount())
                .currency(paymentJpaEntity.getCurrency())
                .status(paymentJpaEntity.getStatus())
                .txnRef(paymentJpaEntity.getTxnRef())
                .paidAt(paymentJpaEntity.getPaidAt())
                .failedAt(paymentJpaEntity.getFailedAt())
                .failureReason(paymentJpaEntity.getFailureReason())
                .description(paymentJpaEntity.getDescription())
                .createdBy(paymentJpaEntity.getCreatedBy())
                .createdAt(paymentJpaEntity.getCreatedAt())
                .updatedBy(paymentJpaEntity.getUpdatedBy())
                .updatedAt(paymentJpaEntity.getUpdatedAt())
                .build();
    }

    public PaymentEntity toEntity(PaymentAggregate paymentAggregate) {
        PaymentEntity paymentJpaEntity = PaymentEntity.builder()
                .id(paymentAggregate.getId())
                .bookingCode(paymentAggregate.getBookingCode())
                .method(paymentAggregate.getMethod())
                .amount(paymentAggregate.getAmount())
                .currency(paymentAggregate.getCurrency())
                .status(paymentAggregate.getStatus())
                .txnRef(paymentAggregate.getTxnRef())
                .paidAt(paymentAggregate.getPaidAt())
                .failedAt(paymentAggregate.getFailedAt())
                .failureReason(paymentAggregate.getFailureReason())
                .description(paymentAggregate.getDescription())
                .build();
        paymentJpaEntity.setCreatedBy(paymentAggregate.getCreatedBy());
        paymentJpaEntity.setCreatedAt(paymentAggregate.getCreatedAt());
        paymentJpaEntity.setUpdatedBy(paymentAggregate.getUpdatedBy());
        paymentJpaEntity.setUpdatedAt(paymentAggregate.getUpdatedAt());
        return paymentJpaEntity;
    }
}
