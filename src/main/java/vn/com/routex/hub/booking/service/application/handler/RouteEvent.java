package vn.com.routex.hub.booking.service.application.handler;

import vn.com.routex.hub.booking.service.infrastructure.kafka.event.RouteSellableEvent;
import vn.com.routex.hub.booking.service.infrastructure.kafka.model.KafkaEventMessage;

public interface RouteEvent {

    void generateRouteSeat(KafkaEventMessage<RouteSellableEvent> payload);
}
