package vn.com.routex.hub.booking.service.infrastructure.kafka.event;

import vn.com.routex.hub.booking.service.infrastructure.cache.redis.models.RouteCacheSeat;

import java.util.List;

public record RouteSeatGeneratedEvent(
        String routeId,
        List<RouteCacheSeat> seats
) {
}