package vn.com.routex.hub.booking.service.application.handler;

import vn.com.routex.hub.booking.service.infrastructure.kafka.event.DomainEvent;
import vn.com.routex.hub.booking.service.infrastructure.kafka.event.PaymentFailedEvent;
import vn.com.routex.hub.booking.service.infrastructure.kafka.event.PaymentSuccessEvent;
import vn.com.routex.hub.booking.service.interfaces.models.base.BaseRequest;

public interface PaymentEvent {
    void updateSuccessPayment(DomainEvent event, BaseRequest context, PaymentSuccessEvent payload);
    void updateFailEvent(DomainEvent event, BaseRequest context, PaymentFailedEvent payload);
}
