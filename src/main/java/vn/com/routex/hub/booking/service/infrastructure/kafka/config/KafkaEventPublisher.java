package vn.com.routex.hub.booking.service.infrastructure.kafka.config;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.infrastructure.kafka.KafkaEventMessage;
import vn.com.routex.hub.booking.service.infrastructure.kafka.KafkaTopicHandler;
import vn.com.routex.hub.booking.service.infrastructure.persistence.utils.JsonUtils;

import java.time.OffsetDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class KafkaEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaTopicHandler topicResolver;

    public void publish(
            String topicKey,
            String eventKey,
            String aggregateId,
            Object payload
    ) {
        try {
            String topic = topicResolver.topicName(topicKey);
            String eventName = topicResolver.eventName(eventKey);

            KafkaEventMessage<Object> message =
                    KafkaEventMessage.builder()
                            .eventId(UUID.randomUUID().toString())
                            .eventName(eventName)
                            .aggregateId(aggregateId)
                            .source("management-service")
                            .version(1)
                            .occurredAt(OffsetDateTime.now())
                            .data(payload)
                            .build();

            String json = JsonUtils.parseToJsonStr(message);

            kafkaTemplate.send(topic, aggregateId, json);
        } catch(Exception ex) {
            throw new IllegalArgumentException("Kafka publish failed: ", ex);
        }
    }
}
