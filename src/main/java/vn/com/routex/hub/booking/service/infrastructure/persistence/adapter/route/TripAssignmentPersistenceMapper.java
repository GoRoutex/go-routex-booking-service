package vn.com.routex.hub.booking.service.infrastructure.persistence.adapter.route;

import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.domain.route.model.TripAssignmentRecord;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.assignment.entity.TripAssignmentEntity;

@Component
public class TripAssignmentPersistenceMapper {

    public TripAssignmentRecord toDomain(TripAssignmentEntity entity) {
        return TripAssignmentRecord.builder()
                .id(entity.getId())
                .tripId(entity.getTripId())
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

    public TripAssignmentEntity toJpaEntity(TripAssignmentRecord record) {
        return TripAssignmentEntity.builder()
                .id(record.getId())
                .tripId(record.getTripId())
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
