package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.routepoint.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.routepoint.entity.RoutePointEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoutePointEntityRepository extends JpaRepository<RoutePointEntity, String> {

    RoutePointEntity findByRouteId(String routeId);

    List<RoutePointEntity> findAllByRouteId(String routeId);

    List<RoutePointEntity> findByRouteIdIn(List<String> routeIds);

    Optional<RoutePointEntity> findByRouteIdAndStopOrder(String routeId, String stopOrder);
}

