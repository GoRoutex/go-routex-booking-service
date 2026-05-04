package vn.com.routex.hub.booking.service.infrastructure.persistence.adapter.seat;

import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.domain.seat.model.TripSeat;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.seat.entity.TripSeatEntity;

@Component
public class TripSeatPersistenceMapper {

    public TripSeat toDomain(TripSeatEntity entity) {
        return TripSeat.builder()
                .id(entity.getId())
                .tripId(entity.getTripId())
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

    public TripSeatEntity toEntity(TripSeat tripSeat) {
        return TripSeatEntity.builder()
                .id(tripSeat.getId())
                .tripId(tripSeat.getTripId())
                .seatNo(tripSeat.getSeatNo())
                .seatTemplateId(tripSeat.getSeatTemplateId())
                .status(tripSeat.getStatus())
                .creator(tripSeat.getCreator())
                .createdAt(tripSeat.getCreatedAt())
                .createdBy(tripSeat.getCreatedBy())
                .updatedAt(tripSeat.getUpdatedAt())
                .updatedBy(tripSeat.getUpdatedBy())
                .build();
    }
}
