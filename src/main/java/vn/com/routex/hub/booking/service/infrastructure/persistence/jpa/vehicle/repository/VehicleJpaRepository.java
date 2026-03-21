package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.vehicle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.vehicle.entity.VehicleJpaEntity;

import java.util.List;

public interface VehicleJpaRepository extends JpaRepository<VehicleJpaEntity, String> {
    boolean existsByVehiclePlate(String vehiclePlate);

    List<VehicleJpaEntity> findByIdIn(List<String> vehicleIds);
}
