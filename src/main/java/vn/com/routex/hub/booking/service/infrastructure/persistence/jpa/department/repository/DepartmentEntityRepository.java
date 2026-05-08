package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.department.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.department.entity.DepartmentEntity;

import java.util.List;
import java.util.Optional;


@Repository
public interface DepartmentEntityRepository extends JpaRepository<DepartmentEntity, String> {
    Optional<DepartmentEntity> findByCode(String code);

    Optional<DepartmentEntity> findByIdAndMerchantId(String id, String merchantId);

    Optional<DepartmentEntity> findByCodeAndMerchantId(String code, String merchantId);

    boolean existsByCode(String code);

    boolean existsByCodeAndMerchantId(String code, String merchantId);

    List<DepartmentEntity> findByMerchantId(String merchantId);

    Page<DepartmentEntity> findByMerchantId(String merchantId, Pageable pageable);
}
