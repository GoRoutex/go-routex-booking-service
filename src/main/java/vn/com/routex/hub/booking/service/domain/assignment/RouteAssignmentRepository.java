package vn.com.routex.hub.booking.service.domain.assignment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteAssignmentRepository extends JpaRepository<RouteAssignment, String> {
    boolean existsByRouteId(String routeId);
    @Query(value = """
            SELECT ra.*
            FROM route_assignment ra
            WHERE ra.route_id IN (:routeIds)
              AND ra.status = :status
              AND ra.unassigned_at IS NULL
        """, nativeQuery = true)
    List<RouteAssignment> findActiveByRouteIdsNative(
            @Param("routeIds") List<String> routeIds,
            @Param("status") String status
    );
}