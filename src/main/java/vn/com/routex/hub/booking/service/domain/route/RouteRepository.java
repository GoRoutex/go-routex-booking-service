package vn.com.routex.hub.booking.service.domain.route;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends JpaRepository<Route, String>, JpaSpecificationExecutor<Route> {

    @Query(value = """
            SELECT generate_route_code(:origin, :destination)
            """, nativeQuery = true)
    String generateRouteCode(@Param("origin") String origin,
                             @Param("destination") String destination);
}
