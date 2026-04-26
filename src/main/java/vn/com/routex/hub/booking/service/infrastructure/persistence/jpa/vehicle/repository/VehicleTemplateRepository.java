package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.vehicle.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.booking.service.domain.vehicle.VehicleTemplateCategory;
import vn.com.routex.hub.booking.service.domain.vehicle.VehicleTemplateStatus;
import vn.com.routex.hub.booking.service.domain.vehicle.VehicleTemplateType;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.vehicle.entity.VehicleTemplateEntity;

import java.util.Optional;

@Repository
public interface VehicleTemplateRepository extends JpaRepository<VehicleTemplateEntity, String> {
    Optional<VehicleTemplateEntity> findFirstByCategoryAndTypeAndStatus(
            VehicleTemplateCategory category,
            VehicleTemplateType type,
            VehicleTemplateStatus status
    );

    Optional<VehicleTemplateEntity> findFirstByCategoryAndTypeAndMerchantIdAndStatus(
            VehicleTemplateCategory category,
            VehicleTemplateType type,
            String merchantId,
            VehicleTemplateStatus status
    );

    Optional<VehicleTemplateEntity> findByIdAndMerchantId(String id, String merchantId);

    boolean existsByCodeAndMerchantId(String code, String merchantId);

    boolean existsByCategoryAndTypeAndMerchantId(
            VehicleTemplateCategory category,
            VehicleTemplateType type,
            String merchantId
    );

    @Query("""
            select vt
            from VehicleTemplateEntity vt
            where vt.merchantId = :merchantId
              and (:status is null or vt.status = :status)
              and (:category is null or vt.category = :category)
              and (:type is null or vt.type = :type)
            """)
    Page<VehicleTemplateEntity> findByFilters(
            @Param("merchantId") String merchantId,
            @Param("status") VehicleTemplateStatus status,
            @Param("category") VehicleTemplateCategory category,
            @Param("type") VehicleTemplateType type,
            Pageable pageable
    );
}
