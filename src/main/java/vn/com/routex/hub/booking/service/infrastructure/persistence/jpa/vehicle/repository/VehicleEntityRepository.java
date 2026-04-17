package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.vehicle.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.vehicle.entity.VehicleEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleEntityRepository extends JpaRepository<VehicleEntity, String> {
    boolean existsByVehiclePlate(String vehiclePlate);

    boolean existsByVehiclePlateAndMerchantId(String vehiclePlate, String merchantId);

    Optional<VehicleEntity> findByIdAndMerchantId(String id, String merchantId);

    List<VehicleEntity> findByIdIn(List<String> vehicleIds);

    List<VehicleEntity> findByIdInAndMerchantId(List<String> vehicleIds, String merchantId);

    List<VehicleEntity> findByMerchantId(String merchantId);

    Page<VehicleEntity> findByMerchantId(String merchantId, Pageable pageable);

    Page<VehicleEntity> findAll(Pageable pageable);
}
