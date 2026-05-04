package vn.com.routex.hub.booking.service.application.handler;

import vn.com.routex.hub.booking.service.infrastructure.kafka.event.DomainEvent;
import vn.com.routex.hub.booking.service.infrastructure.kafka.event.TripSellableEvent;
import vn.com.routex.hub.booking.service.interfaces.models.base.BaseRequest;

public interface TripEvent {

    void generateRouteSeat(DomainEvent event, BaseRequest context, TripSellableEvent payload);
}
