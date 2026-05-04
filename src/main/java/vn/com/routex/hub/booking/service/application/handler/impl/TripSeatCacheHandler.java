package vn.com.routex.hub.booking.service.application.handler.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import vn.com.routex.hub.booking.service.application.handler.TripSeatCacheEvent;
import vn.com.routex.hub.booking.service.infrastructure.cache.redis.service.TripSeatCacheService;
import vn.com.routex.hub.booking.service.infrastructure.kafka.event.TripSeatGeneratedEvent;


@Component
@RequiredArgsConstructor
public class TripSeatCacheHandler implements TripSeatCacheEvent {

    private final TripSeatCacheService tripSeatCacheService;

    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleTripSeatGenerated(TripSeatGeneratedEvent event) {
        tripSeatCacheService.putSeats(event.tripId(), event.seats());
    }
}
