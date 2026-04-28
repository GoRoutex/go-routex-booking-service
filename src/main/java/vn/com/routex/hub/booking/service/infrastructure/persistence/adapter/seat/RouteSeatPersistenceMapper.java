package vn.com.routex.hub.booking.service.infrastructure.persistence.adapter.seat;

import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.domain.seat.model.RouteSeat;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.seat.entity.RouteSeatEntity;

@Component
public class RouteSeatPersistenceMapper {

    public RouteSeat toDomain(RouteSeatEntity entity) {
        return RouteSeat.builder()
                .id(entity.getId())
                .routeId(entity.getRouteId())
                .seatNo(entity.getSeatNo())
                .seatTemplateId(entity.getSeatTemplateId())
                .status(entity.getStatus())
                .creator(entity.getCreator())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public RouteSeatEntity toEntity(RouteSeat routeSeat) {
        return RouteSeatEntity.builder()
                .id(routeSeat.getId())
                .routeId(routeSeat.getRouteId())
                .seatNo(routeSeat.getSeatNo())
                .seatTemplateId(routeSeat.getSeatTemplateId())
                .status(routeSeat.getStatus())
                .creator(routeSeat.getCreator())
                .createdAt(routeSeat.getCreatedAt())
                .createdBy(routeSeat.getCreatedBy())
                .updatedAt(routeSeat.getUpdatedAt())
                .updatedBy(routeSeat.getUpdatedBy())
                .build();
    }
}
