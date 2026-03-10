package vn.com.routex.hub.booking.service.infrastructure.kafka;


import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.ExponentialBackOff;
import vn.com.routex.hub.booking.service.infrastructure.kafka.properties.KafkaEventProperties;

@Configuration
@RequiredArgsConstructor
public class KafkaErrorHandler {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaEventProperties kafkaEventProperties;

    @Bean
    public DefaultErrorHandler defaultErrorHandler() {
        ExponentialBackOff backOff = new ExponentialBackOff();
        backOff.setInitialInterval(kafkaEventProperties.getRetry().getBackOffMs());
        backOff.setMultiplier(kafkaEventProperties.getRetry().getBackOffMultiplier());
        backOff.setMaxAttempts(kafkaEventProperties.getRetry().getMaxAttempts());


        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(
                kafkaTemplate,
                (record, ex) -> new TopicPartition("bus-dead-letter-queue", record.partition())
        );

        return new DefaultErrorHandler(recoverer, backOff);
    }
}
