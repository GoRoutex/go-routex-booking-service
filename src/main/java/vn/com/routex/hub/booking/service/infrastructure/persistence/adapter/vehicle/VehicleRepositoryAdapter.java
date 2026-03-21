package vn.com.routex.hub.booking.service.infrastructure.persistence.adapter.vehicle;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.domain.vehicle.model.VehicleProfile;
import vn.com.routex.hub.booking.service.domain.vehicle.port.VehicleRepositoryPort;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.vehicle.repository.VehicleJpaRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VehicleRepositoryAdapter implements VehicleRepositoryPort {

    private final VehicleJpaRepository vehicleJpaRepository;
    private final VehiclePersistenceMapper vehiclePersistenceMapper;

    @Override
    public Optional<VehicleProfile> findById(String vehicleId) {
        return vehicleJpaRepository.findById(vehicleId)
                .map(vehiclePersistenceMapper::toDomain);
    }
}
