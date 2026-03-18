package vn.com.routex.hub.booking.service.infrastructure.kafka.consumer;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.application.handler.impl.PaymentEventHandler;
import vn.com.routex.hub.booking.service.infrastructure.kafka.event.PaymentFailedEvent;
import vn.com.routex.hub.booking.service.infrastructure.kafka.event.PaymentSuccessEvent;
import vn.com.routex.hub.booking.service.infrastructure.kafka.model.KafkaEventMessage;
import vn.com.routex.hub.booking.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.booking.service.infrastructure.persistence.log.SystemLog;
import vn.com.routex.hub.booking.service.infrastructure.persistence.utils.ExceptionUtils;
import vn.com.routex.hub.booking.service.infrastructure.persistence.utils.JsonUtils;

import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.INVALID_DATA_ERROR_MESSAGE;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.INVALID_EVENT_MESSAGE;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.INVALID_INPUT_ERROR;

@Component
@RequiredArgsConstructor
public class PaymentEventConsumer {

    @Value("${spring.kafka.events.payment-completed}")
    private String paymentCompletedEvent;

    @Value("${spring.kafka.events.payment-failed}")
    private String paymentFailedEvent;

    private final PaymentEventHandler paymentEventHandler;


    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @KafkaListener(topics = "${spring.kafka.topics.payment-success}",
    groupId = "${spring.kafka.group-id.payments}")
    public void paymentCompletedConsumer(String payload) {
        KafkaEventMessage<PaymentSuccessEvent>  event =
                JsonUtils.parseToKafkaObject(
                        payload,
                        new TypeReference<>() {
                        });

        if (event == null || event.data() == null) {
            throw new BusinessException(ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_DATA_ERROR_MESSAGE));
        }

        if (!paymentCompletedEvent.equals(event.eventName())) {
            sLog.info("Ignore event {}", event.eventName());
            return;
        }

        sLog.info("[PAYMENT-EVENT] Processing event: eventName={} eventId={} aggregateId={} paymentId={} customerId={}",
                event.eventName(),
                event.eventId(),
                event.aggregateId(),
                event.data().paymentId(),
                event.data().customerId());

        PaymentSuccessEvent data = event.data();

        if (event.eventId().isBlank()
                || event.eventName().isBlank()
                || event.aggregateId().isBlank()
                || data.paymentId().isBlank()
                || data.customerId().isBlank()
                || data.status() == null) {
            throw new BusinessException(event.requestId(), event.requestDateTime(), event.channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, String.format(INVALID_EVENT_MESSAGE, event.eventName())));
        }

        paymentEventHandler.updateSuccessPayment(event);
        sLog.info("[PAYMENT-EVENT] Event processed successfully: eventName={} eventId={} paymentId={}", event.eventName(), event.eventId(), event.aggregateId());

        // Publish event for notification
        // Publish event for analytics
    }

    @KafkaListener(topics = "${spring.kafka.topics.payment-failed}",
            groupId = "${spring.kafka.group-id.payments}")
    public void paymentFailedConsumer(String payload) {
        KafkaEventMessage<PaymentFailedEvent>  event =
                JsonUtils.parseToKafkaObject(
                        payload,
                        new TypeReference<>() {
                        });

        if (event == null || event.data() == null) {
            throw new BusinessException(ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_DATA_ERROR_MESSAGE));
        }

        if (!paymentFailedEvent.equals(event.eventName())) {
            sLog.info("Ignore event {}", event.eventName());
            return;
        }

        sLog.info("[PAYMENT-EVENT] Processing event: eventName={} eventId={} aggregateId={} paymentId={}",
                event.eventName(),
                event.eventId(),
                event.aggregateId(),
                event.data().paymentId());

        paymentEventHandler.updateFailEvent(event);
        sLog.info("[BOOKING-EVENT] Event processed successfully: eventName={} eventId={} paymentId={}", event.eventName(), event.eventId(), event.aggregateId());
        // Publish event for notification
        // Publish event for analytics
    }
}
