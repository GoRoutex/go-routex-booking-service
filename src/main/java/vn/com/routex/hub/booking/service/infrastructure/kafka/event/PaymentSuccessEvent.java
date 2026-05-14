package vn.com.routex.hub.booking.service.infrastructure.kafka.event;


import lombok.Builder;
import vn.com.routex.hub.booking.service.domain.booking.PaymentStatus;

import java.math.BigDecimal;

@Builder
public record PaymentSuccessEvent(
        String paymentId,
        String customerId,
        String bookingCode,
        BigDecimal amount,
        PaymentStatus status,
        String currency
) {
}
