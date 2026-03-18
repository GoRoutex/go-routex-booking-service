package vn.com.routex.hub.booking.service.domain.fare;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FareConfigRepository extends JpaRepository<FareConfig, Integer> {

    Optional<FareConfig> findByVehicleType(String vehicleType);
}
