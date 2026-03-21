package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.fare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.fare.entity.FareConfigJpaEntity;

import java.util.Optional;

public interface FareConfigJpaRepository extends JpaRepository<FareConfigJpaEntity, Integer> {

    Optional<FareConfigJpaEntity> findByVehicleType(String vehicleType);
}
