package vn.com.routex.hub.booking.service.infrastructure.persistence.adapter.payment;

import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.domain.payment.model.Payment;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.payment.entity.PaymentEntity;

@Component
public class PaymentPersistenceMapper {

    public Payment toDomain(PaymentEntity entity) {
        return Payment.builder()
                .id(entity.getId())
                .bookingId(entity.getBookingId())
                .code(entity.getCode())
                .method(entity.getMethod())
                .amount(entity.getAmount())
                .currency(entity.getCurrency())
                .status(entity.getStatus())
                .checkoutUrl(entity.getCheckoutUrl())
                .paymentToken(entity.getPaymentToken())
                .paidAt(entity.getPaidAt())
                .expiredAt(entity.getExpiredAt())
                .failedAt(entity.getFailedAt())
                .failureReason(entity.getFailureReason())
                .description(entity.getDescription())
                .build();
    }

    public PaymentEntity toJpaEntity(Payment payment) {
        return PaymentEntity.builder()
                .id(payment.getId())
                .bookingId(payment.getBookingId())
                .code(payment.getCode())
                .method(payment.getMethod())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .status(payment.getStatus())
                .checkoutUrl(payment.getCheckoutUrl())
                .paymentToken(payment.getPaymentToken())
                .paidAt(payment.getPaidAt())
                .expiredAt(payment.getExpiredAt())
                .failedAt(payment.getFailedAt())
                .failureReason(payment.getFailureReason())
                .description(payment.getDescription())
                .build();
    }
}
