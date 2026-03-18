package vn.com.routex.hub.booking.service.infrastructure.kafka.model;

import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record KafkaEventMessage<T> (
        String requestId,
        String requestDateTime,
        String channel,
        String eventId,
        String eventName,
        String aggregateId,
        String source,
        Integer version,
        OffsetDateTime occurredAt,
        T data
) {
}
