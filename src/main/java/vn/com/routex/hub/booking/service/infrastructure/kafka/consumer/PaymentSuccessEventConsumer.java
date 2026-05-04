package vn.com.routex.hub.booking.service.infrastructure.kafka.consumer;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.hub.booking.service.application.handler.impl.PaymentEventHandler;
import vn.com.routex.hub.booking.service.infrastructure.kafka.event.DomainEvent;
import vn.com.routex.hub.booking.service.infrastructure.kafka.event.PaymentSuccessEvent;
import vn.com.routex.hub.booking.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.booking.service.infrastructure.persistence.utils.ExceptionUtils;
import vn.com.routex.hub.booking.service.infrastructure.persistence.utils.JsonUtils;
import vn.com.routex.hub.booking.service.interfaces.models.base.BaseRequest;

import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.INVALID_DATA_ERROR_MESSAGE;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.INVALID_EVENT_MESSAGE;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.INVALID_INPUT_ERROR;

@Component
@RequiredArgsConstructor
public class PaymentSuccessEventConsumer {

    @Value("${spring.kafka.events.payment-succeeded}")
    private String paymentSucceededEvent;

    private final PaymentEventHandler paymentEventHandler;
    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @KafkaListener(
            topics = "${spring.kafka.topics.payments}",
            containerFactory = "kafkaListenerContainerFactory",
            groupId = "${spring.kafka.group-id.payment-succeeded}"
    )
    public void paymentCompletedConsumer(String payload, Acknowledgment acknowledgment) {
        sLog.info("[PAYMENT-EVENTS] Raw Payload: {}", payload);
        DomainEvent event =
                JsonUtils.parseToKafkaObject(
                        payload,
                        new TypeReference<>() {
                        });


        sLog.info("[PAYMENT-EVENT] Domain Event: {}", event);

        if (event == null
                || event.header() == null
                || event.payload() == null
                || event.header().get("context") == null
                || event.payload().get("data") == null) {
            throw new BusinessException(ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_DATA_ERROR_MESSAGE));
        }

        if (!paymentSucceededEvent.equals(event.eventType())) {
            sLog.info("Ignore event {}", event.eventType());
            acknowledgment.acknowledge();
            return;
        }

        BaseRequest context = JsonUtils.convertValue(event.header().get("context"), BaseRequest.class);
        PaymentSuccessEvent paymentEvent = JsonUtils.convertValue(event.payload().get("data"), PaymentSuccessEvent.class);

        sLog.info("[PAYMENT-EVENT] Processing event: eventName={} eventId={} aggregateId={} paymentId={} customerId={}",
                event.eventType(),
                event.eventId(),
                event.aggregateId(),
                paymentEvent.paymentId(),
                paymentEvent.customerId());


        try {
            validateEvent(event, context, paymentEvent);
            paymentEventHandler.updateSuccessPayment(event, context, paymentEvent);
            sLog.info("[PAYMENT-EVENT] Event processed successfully: eventName={} eventId={} paymentId={}", event.eventType(), event.eventId(), event.aggregateId());
            acknowledgment.acknowledge();
        } catch(Exception ex) {
            sLog.error("[PAYMENT-EVENT] Failed eventName={} eventId={} aggregateId={} paymentId={} bookingCode={}",
                    event.eventType(),
                    event.eventId(),
                    event.aggregateId(),
                    paymentEvent.paymentId(),
                    paymentEvent.bookingCode(),
                    ex);
            throw ex;
        }
        // Publish event for notification
        // Publish event for analytics
    }

    private void validateEvent(DomainEvent event, BaseRequest context, PaymentSuccessEvent data) {
        if (event.eventId().isBlank()
                || event.eventType().isBlank()
                || event.aggregateId().isBlank()
                || context == null
                || context.getRequestId().isBlank()
                || context.getRequestDateTime().isBlank()
                || context.getChannel().isBlank()
                || data.paymentId().isBlank()
                || data.status() == null) {
            throw new BusinessException(ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, String.format(INVALID_EVENT_MESSAGE, event.eventType())));
        }
    }
}
