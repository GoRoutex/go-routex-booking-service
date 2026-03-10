package vn.com.routex.hub.booking.service.infrastructure.kafka;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import vn.com.routex.hub.booking.service.infrastructure.kafka.properties.KafkaEventProperties;

@Component
@RequiredArgsConstructor
public class KafkaTopicHandler {

    private final KafkaEventProperties kafkaEventProperties;

    public String eventName(String key) {
        String value = kafkaEventProperties.getEvents().get(key);
        if(value == null || value.isBlank()) {
            throw new IllegalArgumentException("Kafka event key not configured: " + key);
        }
        return value;
    }

    public String topicName(String key) {
        String value = kafkaEventProperties.getTopics().get(key);
        if(value == null || value.isBlank()) {
            throw new IllegalArgumentException("Kafka topic not configured: " + key);
        }
        return value;
    }
}
