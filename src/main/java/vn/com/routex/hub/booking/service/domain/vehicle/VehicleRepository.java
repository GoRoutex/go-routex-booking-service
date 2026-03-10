package vn.com.routex.hub.booking.service.domain.vehicle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {
    boolean existsByVehiclePlate(String vehiclePlate);

    List<Vehicle> findByIdIn(List<String> vehicleIds);
}
