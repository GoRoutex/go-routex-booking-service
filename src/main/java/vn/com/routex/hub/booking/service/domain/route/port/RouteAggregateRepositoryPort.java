package vn.com.routex.hub.booking.service.domain.route.port;



import vn.com.routex.hub.booking.service.domain.route.model.RouteAggregate;

import java.util.List;
import java.util.Optional;

public interface RouteAggregateRepositoryPort {
    Optional<RouteAggregate> findById(String routeId);

    Optional<RouteAggregate> findById(String routeId, String merchantId);

    List<RouteAggregate> findByMerchantId(String merchantId);

    void save(RouteAggregate aggregate);

    String generateRouteCode(String originCode, String destinationCode);
}
