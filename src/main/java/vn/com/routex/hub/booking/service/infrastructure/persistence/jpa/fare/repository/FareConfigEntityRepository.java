package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.fare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.fare.entity.FareConfigEntity;

import java.util.Optional;

public interface FareConfigEntityRepository extends JpaRepository<FareConfigEntity, Integer> {

    Optional<FareConfigEntity> findByVehicleType(String vehicleType);
}
