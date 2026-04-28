package vn.com.routex.hub.booking.service.infrastructure.persistence.adapter.route;

import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.domain.route.model.RouteAssignmentRecord;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.assignment.entity.RouteAssignmentEntity;

@Component
public class RouteAssignmentPersistenceMapper {

    public RouteAssignmentRecord toDomain(RouteAssignmentEntity entity) {
        return RouteAssignmentRecord.builder()
                .id(entity.getId())
                .routeId(entity.getRouteId())
                .creator(entity.getCreator())
                .merchantId(entity.getMerchantId())
                .vehicleId(entity.getVehicleId())
                .driverId(entity.getDriverId())
                .ticketPrice(entity.getTicketPrice())
                .assignedAt(entity.getAssignedAt())
                .unAssignedAt(entity.getUnAssignedAt())
                .status(entity.getStatus())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public RouteAssignmentEntity toJpaEntity(RouteAssignmentRecord record) {
        return RouteAssignmentEntity.builder()
                .id(record.getId())
                .routeId(record.getRouteId())
                .creator(record.getCreator())
                .merchantId(record.getMerchantId())
                .driverId(record.getDriverId())
                .vehicleId(record.getVehicleId())
                .ticketPrice(record.getTicketPrice())
                .assignedAt(record.getAssignedAt())
                .unAssignedAt(record.getUnAssignedAt())
                .status(record.getStatus())
                .updatedAt(record.getUpdatedAt())
                .updatedBy(record.getUpdatedBy())
                .build();
    }
}
