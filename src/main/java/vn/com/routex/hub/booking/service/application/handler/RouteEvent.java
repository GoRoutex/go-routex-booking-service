package vn.com.routex.hub.booking.service.application.handler;

import vn.com.routex.hub.booking.service.interfaces.models.base.BaseRequest;
import vn.com.routex.hub.booking.service.infrastructure.kafka.event.DomainEvent;
import vn.com.routex.hub.booking.service.infrastructure.kafka.event.RouteSellableEvent;

public interface RouteEvent {

    void generateRouteSeat(DomainEvent event, BaseRequest context, RouteSellableEvent payload);
}
