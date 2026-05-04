package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.booking.service.domain.assignment.TripAssignmentStatus;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.assignment.entity.TripAssignmentEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface TripAssignmentEntityRepository extends JpaRepository<TripAssignmentEntity, String> {
    boolean existsByTripId(String tripId);

    boolean existsByTripIdAndMerchantId(String tripId, String merchantId);

    Optional<TripAssignmentEntity> findFirstByTripIdAndStatusAndUnAssignedAtIsNullOrderByAssignedAtDesc(String tripId, TripAssignmentStatus status);

    Optional<TripAssignmentEntity> findFirstByTripIdAndMerchantIdAndStatusAndUnAssignedAtIsNullOrderByAssignedAtDesc(
            String tripId,
            String merchantId,
            TripAssignmentStatus status
    );

    List<TripAssignmentEntity> findByMerchantId(String merchantId);

    @Query(value = """
            SELECT ra.*
            FROM trip_assignment ra
            WHERE ra.trip_id IN (:tripIds)
              AND ra.status = :status
              AND ra.unassigned_at IS NULL
        """, nativeQuery = true)
    List<TripAssignmentEntity> findActiveByTripIdsNative(
            @Param("tripIds") List<String> tripIds,
            @Param("status") String status
    );

    @Query(value = """
            SELECT ra.*
            FROM trip_assignment ra
            WHERE ra.trip_id IN (:tripIds)
              AND ra.merchant_id = :merchantId
              AND ra.status = :status
              AND ra.unassigned_at IS NULL
        """, nativeQuery = true)
    List<TripAssignmentEntity> findActiveByTripIdsAndMerchantIdNative(
            @Param("tripIds") List<String> tripIds,
            @Param("merchantId") String merchantId,
            @Param("status") String status
    );

    Optional<TripAssignmentEntity> findByTripId(String tripId);
}
