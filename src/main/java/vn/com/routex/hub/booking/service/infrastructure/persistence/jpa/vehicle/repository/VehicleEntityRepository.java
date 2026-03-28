package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.vehicle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.vehicle.entity.VehicleEntity;

import java.util.List;

public interface VehicleEntityRepository extends JpaRepository<VehicleEntity, String> {
    boolean existsByVehiclePlate(String vehiclePlate);

    List<VehicleEntity> findByIdIn(List<String> vehicleIds);
}
