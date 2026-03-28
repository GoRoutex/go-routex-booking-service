package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.stoppoint.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.stoppoint.entity.StopPointEntity;

@Repository
public interface StopPointEntityRepository extends JpaRepository<StopPointEntity, String> {
}

