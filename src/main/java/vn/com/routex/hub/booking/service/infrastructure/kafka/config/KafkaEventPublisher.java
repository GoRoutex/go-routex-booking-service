package vn.com.routex.hub.booking.service.infrastructure.kafka.config;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.controller.models.base.BaseRequest;
import vn.com.routex.hub.booking.service.infrastructure.kafka.model.KafkaEventMessage;
import vn.com.routex.hub.booking.service.infrastructure.persistence.utils.JsonUtils;

import java.time.OffsetDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class KafkaEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void publish(
            BaseRequest request,
            String topicName,
            String eventName,
            String aggregateId,
            Object payload
    ) {
        try {

            KafkaEventMessage<Object> message =
                    KafkaEventMessage.builder()
                            .eventId(UUID.randomUUID().toString())
                            .eventType(eventName)
                            .aggregateId(aggregateId)
                            .eventKey(eventName)
                            .header(KafkaEventMessage.MessageHeader.builder()
                                    .context(KafkaEventMessage.MessageContext.builder()
                                            .requestId(request.getRequestId())
                                            .requestDateTime(request.getRequestDateTime())
                                            .channel(request.getChannel())
                                            .build())
                                    .build())
                            .payload(KafkaEventMessage.MessagePayload.builder()
                                    .data(payload)
                                    .build())
                            .source("booking-service")
                            .version(1)
                            .occurredAt(OffsetDateTime.now())
                            .build();

            String json = JsonUtils.parseToJsonStr(message);

            kafkaTemplate.send(topicName, aggregateId, json);
        } catch(Exception ex) {
            throw new IllegalArgumentException("Kafka publish failed: ", ex);
        }
    }

    public void publish(
            String requestId,
            String requestDateTime,
            String channel,
            String topicName,
            String eventName,
            String aggregateId,
            Object payload
    ) {
        try {
            KafkaEventMessage<Object> message =
                    KafkaEventMessage.builder()
                            .eventId(UUID.randomUUID().toString())
                            .eventType(eventName)
                            .aggregateId(aggregateId)
                            .eventKey(eventName)
                            .header(KafkaEventMessage.MessageHeader.builder()
                                    .context(KafkaEventMessage.MessageContext.builder()
                                            .requestId(requestId)
                                            .requestDateTime(requestDateTime)
                                            .channel(channel)
                                            .build())
                                    .build())
                            .payload(KafkaEventMessage.MessagePayload.builder()
                                    .data(payload)
                                    .build())
                            .source("booking-service")
                            .version(1)
                            .occurredAt(OffsetDateTime.now())
                            .build();

            String json = JsonUtils.parseToJsonStr(message);
            kafkaTemplate.send(topicName, aggregateId, json);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Kafka publish failed: ", ex);
        }
    }
}
