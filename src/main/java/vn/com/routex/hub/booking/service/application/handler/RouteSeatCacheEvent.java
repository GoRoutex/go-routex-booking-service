package vn.com.routex.hub.booking.service.application.handler;

import vn.com.routex.hub.booking.service.infrastructure.kafka.event.RouteSeatGeneratedEvent;

public interface RouteSeatCacheEvent {
    void handleRouteSeatGenerated(RouteSeatGeneratedEvent event);
}
