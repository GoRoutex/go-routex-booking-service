package vn.com.routex.hub.booking.service.domain.route.port;


import vn.com.routex.hub.booking.service.infrastructure.kafka.event.RouteSellableEvent;

public interface RouteSaleEventPort {
    void publishRouteReadyForSale(
            String requestId,
            String requestDateTime,
            String channel,
            String aggregateId,
            RouteSellableEvent payload
    );
}
