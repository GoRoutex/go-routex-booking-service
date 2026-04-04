package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.operation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.operation.entity.RouteStopEntity;

@Repository
public interface RouteStopEntityRepository extends JpaRepository<RouteStopEntity, String> {
}

