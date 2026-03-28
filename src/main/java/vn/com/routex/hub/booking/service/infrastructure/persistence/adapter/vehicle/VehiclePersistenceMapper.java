package vn.com.routex.hub.booking.service.infrastructure.persistence.adapter.vehicle;

import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.domain.vehicle.model.VehicleProfile;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.vehicle.entity.VehicleEntity;

@Component
public class VehiclePersistenceMapper {

    public VehicleProfile toDomain(VehicleEntity entity) {
        return VehicleProfile.builder()
                .id(entity.getId())
                .creator(entity.getCreator())
                .status(entity.getStatus())
                .type(entity.getType())
                .vehiclePlate(entity.getVehiclePlate())
                .seatCapacity(entity.getSeatCapacity())
                .hasFloor(entity.isHasFloor())
                .manufacturer(entity.getManufacturer())
                .build();
    }
}
