package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.stoppoint.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.stoppoint.entity.RouteStopEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoutePointEntityRepository extends JpaRepository<RouteStopEntity, String> {

    RouteStopEntity findByRouteId(String routeId);

    List<RouteStopEntity> findAllByRouteId(String routeId);

    List<RouteStopEntity> findByRouteIdIn(List<String> routeIds);

    Optional<RouteStopEntity> findByRouteIdAndStopOrder(String routeId, String stopOrder);
}

