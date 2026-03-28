package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.stoppoint.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.stoppoint.entity.RouteStopEntity;

@Repository
public interface RouteStopEntityRepository extends JpaRepository<RouteStopEntity, String> {
}

