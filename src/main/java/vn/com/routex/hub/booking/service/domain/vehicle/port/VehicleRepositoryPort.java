package vn.com.routex.hub.booking.service.domain.vehicle.port;

import vn.com.routex.hub.booking.service.domain.vehicle.model.VehicleProfile;

import java.util.Optional;

public interface VehicleRepositoryPort {
    Optional<VehicleProfile> findById(String vehicleId);
}
