package vn.com.routex.hub.booking.service.infrastructure.persistence.adapter.route;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.domain.route.model.RouteAggregate;
import vn.com.routex.hub.booking.service.domain.route.port.RouteAggregateRepositoryPort;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.route.repository.RouteEntityRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RouteAggregateRepositoryAdapter implements RouteAggregateRepositoryPort {

    private final RouteEntityRepository routeEntityRepository;
    private final RoutePersistenceMapper routePersistenceMapper;

    @Override
    public Optional<RouteAggregate> findById(String routeId) {
        return routeEntityRepository.findById(routeId).map(routePersistenceMapper::toDomain);
    }

    @Override
    public Optional<RouteAggregate> findById(String routeId, String merchantId) {
        return routeEntityRepository.findByIdAndMerchantId(routeId, merchantId)
                .map(routePersistenceMapper::toDomain);
    }

    @Override
    public List<RouteAggregate> findByMerchantId(String merchantId) {
        return routeEntityRepository.findByMerchantId(merchantId).stream()
                .map(routePersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public void save(RouteAggregate aggregate) {
        routeEntityRepository.save(routePersistenceMapper.toJpaEntity(aggregate));
    }
}
