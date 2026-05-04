package vn.com.routex.hub.booking.service.application.handler;

import vn.com.routex.hub.booking.service.infrastructure.kafka.event.TripSeatGeneratedEvent;

public interface TripSeatCacheEvent {
    void handleTripSeatGenerated(TripSeatGeneratedEvent event);
}
