package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.route.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.route.entity.RouteEntity;

@Repository
public interface RouteEntityRepository extends JpaRepository<RouteEntity, String>, JpaSpecificationExecutor<RouteEntity> {

    @Query(value = """
            SELECT generate_route_code(:origin, :destination)
            """, nativeQuery = true)
    String generateRouteCode(@Param("origin") String origin,
                             @Param("destination") String destination);
}
