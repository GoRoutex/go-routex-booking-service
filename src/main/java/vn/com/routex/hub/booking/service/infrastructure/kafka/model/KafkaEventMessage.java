package vn.com.routex.hub.booking.service.infrastructure.kafka.model;

import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record KafkaEventMessage<T>(
        String eventId,
        String eventType,
        String aggregateId,
        String eventKey,
        OffsetDateTime occurredAt,
        MessageHeader header,
        MessagePayload<T> payload,
        String source,
        Integer version
) {
    public String eventName() {
        return eventType;
    }

    public String requestId() {
        return header != null && header.context() != null ? header.context().requestId() : null;
    }

    public String requestDateTime() {
        return header != null && header.context() != null ? header.context().requestDateTime() : null;
    }

    public String channel() {
        return header != null && header.context() != null ? header.context().channel() : null;
    }

    public T data() {
        return payload != null ? payload.data() : null;
    }

    @Builder
    public record MessageHeader(
            MessageContext context
    ) {
    }

    @Builder
    public record MessageContext(
            String requestId,
            String requestDateTime,
            String channel
    ) {
    }

    @Builder
    public record MessagePayload<T>(
            T data
    ) {
    }
}
