package vn.com.routex.hub.booking.service.infrastructure.kafka.event;


import lombok.Builder;
import vn.com.routex.hub.booking.service.domain.booking.PaymentStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Builder
public record PaymentSuccessEvent(
        String paymentId,
        String customerId,
        String bookingCode,
        BigDecimal amount,
        PaymentStatus status,
        OffsetDateTime paidAt,
        String currency
) {
}
