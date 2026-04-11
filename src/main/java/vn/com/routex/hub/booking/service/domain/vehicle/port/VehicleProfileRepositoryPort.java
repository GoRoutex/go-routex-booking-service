package vn.com.routex.hub.booking.service.domain.vehicle.port;


import vn.com.routex.hub.booking.service.domain.common.PagedResult;
import vn.com.routex.hub.booking.service.domain.vehicle.model.VehicleProfile;

import java.util.List;
import java.util.Optional;

public interface VehicleProfileRepositoryPort {
    boolean existsByVehiclePlate(String vehiclePlate);

    boolean existsByVehiclePlate(String vehiclePlate, String merchantId);

    Optional<VehicleProfile> findById(String id);

    Optional<VehicleProfile> findById(String id, String merchantId);

    List<VehicleProfile> findByMerchantId(String merchantId);

    void save(VehicleProfile vehicleProfile);

    PagedResult<VehicleProfile> fetch(int pageNumber, int pageSize);

    PagedResult<VehicleProfile> fetch(String merchantId, int pageNumber, int pageSize);
}
