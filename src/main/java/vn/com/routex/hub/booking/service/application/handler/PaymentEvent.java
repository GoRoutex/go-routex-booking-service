package vn.com.routex.hub.booking.service.application.handler;

import vn.com.routex.hub.booking.service.infrastructure.kafka.event.PaymentFailedEvent;
import vn.com.routex.hub.booking.service.infrastructure.kafka.event.PaymentSuccessEvent;
import vn.com.routex.hub.booking.service.infrastructure.kafka.model.KafkaEventMessage;

public interface PaymentEvent {
    void updateSuccessPayment(KafkaEventMessage<PaymentSuccessEvent> event);
    void updateFailEvent(KafkaEventMessage<PaymentFailedEvent> event);
}
