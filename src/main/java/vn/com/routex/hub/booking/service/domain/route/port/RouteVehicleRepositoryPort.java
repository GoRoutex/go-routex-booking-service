package vn.com.routex.hub.booking.service.domain.route.port;



import vn.com.routex.hub.booking.service.domain.route.model.VehicleSnapshot;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RouteVehicleRepositoryPort {
    Optional<VehicleSnapshot> findById(String vehicleId);

    Optional<VehicleSnapshot> findById(String vehicleId, String merchantId);

    Map<String, VehicleSnapshot> findByIds(List<String> vehicleIds);

    Map<String, VehicleSnapshot> findByIds(List<String> vehicleIds, String merchantId);
}
