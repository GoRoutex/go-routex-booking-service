package vn.com.routex.hub.booking.service.infrastructure.kafka.event;

import vn.com.routex.hub.booking.service.infrastructure.cache.redis.models.TripCacheSeat;

import java.util.List;

public record TripSeatGeneratedEvent(
        String tripId,
        List<TripCacheSeat> seats
) {
}