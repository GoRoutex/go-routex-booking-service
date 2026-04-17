package vn.com.routex.hub.booking.service.infrastructure.persistence.adapter.route;

import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.domain.route.model.RouteAggregate;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.route.entity.RouteEntity;

@Component
public class RoutePersistenceMapper {

    public RouteAggregate toDomain(RouteEntity entity) {
        return RouteAggregate.builder()
                .id(entity.getId())
                .routeCode(entity.getRouteCode())
                .creator(entity.getCreator())
                .merchantId(entity.getMerchantId())
                .pickupBranch(entity.getPickupBranch())
                .origin(entity.getOrigin())
                .destination(entity.getDestination())
                .plannedStartTime(entity.getPlannedStartTime())
                .plannedEndTime(entity.getPlannedEndTime())
                .actualStartTime(entity.getActualStartTime())
                .actualEndTime(entity.getActualEndTime())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public RouteEntity toJpaEntity(RouteAggregate aggregate) {
        return RouteEntity.builder()
                .id(aggregate.getId())
                .routeCode(aggregate.getRouteCode())
                .creator(aggregate.getCreator())
                .merchantId(aggregate.getMerchantId())
                .pickupBranch(aggregate.getPickupBranch())
                .origin(aggregate.getOrigin())
                .destination(aggregate.getDestination())
                .plannedStartTime(aggregate.getPlannedStartTime())
                .plannedEndTime(aggregate.getPlannedEndTime())
                .actualStartTime(aggregate.getActualStartTime())
                .actualEndTime(aggregate.getActualEndTime())
                .status(aggregate.getStatus())
                .createdAt(aggregate.getCreatedAt())
                .createdBy(aggregate.getCreatedBy())
                .updatedAt(aggregate.getUpdatedAt())
                .updatedBy(aggregate.getUpdatedBy())
                .build();
    }
}
