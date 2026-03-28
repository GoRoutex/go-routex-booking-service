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
                .status(entity.getStatus())
                .creator(entity.getCreator())
                .build();
    }

    public RouteSeatEntity toJpaEntity(RouteSeat routeSeat) {
        return RouteSeatEntity.builder()
                .id(routeSeat.getId())
                .routeId(routeSeat.getRouteId())
                .seatNo(routeSeat.getSeatNo())
                .status(routeSeat.getStatus())
                .creator(routeSeat.getCreator())
                .build();
    }
}
