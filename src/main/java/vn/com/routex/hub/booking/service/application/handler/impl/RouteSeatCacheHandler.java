package vn.com.routex.hub.booking.service.application.handler.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import vn.com.routex.hub.booking.service.application.handler.RouteSeatCacheEvent;
import vn.com.routex.hub.booking.service.infrastructure.cache.redis.service.RouteSeatCacheService;
import vn.com.routex.hub.booking.service.infrastructure.kafka.event.RouteSeatGeneratedEvent;


@Component
@RequiredArgsConstructor
public class RouteSeatCacheHandler implements RouteSeatCacheEvent {

    private final RouteSeatCacheService routeSeatCacheService;

    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleRouteSeatGenerated(RouteSeatGeneratedEvent event) {
        routeSeatCacheService.putSeats(event.routeId(), event.seats());
    }
}
