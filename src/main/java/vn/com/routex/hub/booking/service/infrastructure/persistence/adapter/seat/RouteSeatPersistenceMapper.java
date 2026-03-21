package vn.com.routex.hub.booking.service.infrastructure.persistence.adapter.seat;

import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.domain.seat.model.RouteSeat;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.seat.entity.RouteSeatJpaEntity;

@Component
public class RouteSeatPersistenceMapper {

    public RouteSeat toDomain(RouteSeatJpaEntity entity) {
        return RouteSeat.builder()
                .id(entity.getId())
                .routeId(entity.getRouteId())
                .seatNo(entity.getSeatNo())
                .status(entity.getStatus())
                .creator(entity.getCreator())
                .build();
    }

    public RouteSeatJpaEntity toJpaEntity(RouteSeat routeSeat) {
        return RouteSeatJpaEntity.builder()
                .id(routeSeat.getId())
                .routeId(routeSeat.getRouteId())
                .seatNo(routeSeat.getSeatNo())
                .status(routeSeat.getStatus())
                .creator(routeSeat.getCreator())
                .build();
    }
}
