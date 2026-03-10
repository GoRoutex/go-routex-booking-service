package vn.com.routex.hub.booking.service.infrastructure.kafka.config;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.infrastructure.kafka.model.KafkaEventMessage;
import vn.com.routex.hub.booking.service.infrastructure.persistence.utils.JsonUtils;
import vn.com.routex.hub.booking.service.interfaces.models.base.BaseRequest;

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
                            .requestId(request.getRequestId())
                            .requestDateTime(request.getRequestDateTime())
                            .channel(request.getChannel())
                            .eventId(UUID.randomUUID().toString())
                            .eventName(eventName)
                            .aggregateId(aggregateId)
                            .source("booking-service")
                            .version(1)
                            .occurredAt(OffsetDateTime.now())
                            .data(payload)
                            .build();

            String json = JsonUtils.parseToJsonStr(message);

            kafkaTemplate.send(topicName, aggregateId, json);
        } catch(Exception ex) {
            throw new IllegalArgumentException("Kafka publish failed: ", ex);
        }
    }
}
