package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.booking.service.domain.assignment.RouteAssignmentStatus;
import vn.com.routex.hub.booking.service.domain.route.model.RouteAssignmentRecord;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.assignment.entity.RouteAssignmentEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouteAssignmentEntityRepository extends JpaRepository<RouteAssignmentEntity, String> {
    boolean existsByRouteId(String routeId);

    boolean existsByRouteIdAndMerchantId(String routeId, String merchantId);

    Optional<RouteAssignmentEntity> findFirstByRouteIdAndStatusAndUnAssignedAtIsNullOrderByAssignedAtDesc(String routeId, RouteAssignmentStatus status);

    Optional<RouteAssignmentEntity> findFirstByRouteIdAndMerchantIdAndStatusAndUnAssignedAtIsNullOrderByAssignedAtDesc(
            String routeId,
            String merchantId,
            RouteAssignmentStatus status
    );

    List<RouteAssignmentEntity> findByMerchantId(String merchantId);

    @Query(value = """
            SELECT ra.*
            FROM route_assignment ra
            WHERE ra.route_id IN (:routeIds)
              AND ra.status = :status
              AND ra.unassigned_at IS NULL
        """, nativeQuery = true)
    List<RouteAssignmentEntity> findActiveByRouteIdsNative(
            @Param("routeIds") List<String> routeIds,
            @Param("status") String status
    );

    @Query(value = """
            SELECT ra.*
            FROM route_assignment ra
            WHERE ra.route_id IN (:routeIds)
              AND ra.merchant_id = :merchantId
              AND ra.status = :status
              AND ra.unassigned_at IS NULL
        """, nativeQuery = true)
    List<RouteAssignmentEntity> findActiveByRouteIdsAndMerchantIdNative(
            @Param("routeIds") List<String> routeIds,
            @Param("merchantId") String merchantId,
            @Param("status") String status
    );

    Optional<RouteAssignmentEntity> findByRouteId(String routeId);
}
