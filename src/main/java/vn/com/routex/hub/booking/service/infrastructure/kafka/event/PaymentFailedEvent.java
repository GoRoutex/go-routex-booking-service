package vn.com.routex.hub.booking.service.infrastructure.kafka.event;

import lombok.Builder;
import vn.com.routex.hub.booking.service.domain.booking.PaymentStatus;

@Builder
public record PaymentFailedEvent(
        String paymentId,
        String bookingCode,
        PaymentStatus status,
        String reason
) {
}
