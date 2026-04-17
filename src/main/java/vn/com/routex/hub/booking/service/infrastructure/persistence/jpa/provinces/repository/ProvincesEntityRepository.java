package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.provinces.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.provinces.entity.ProvincesEntity;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.provinces.projection.ProvincesCodeProjection;

import java.util.Optional;

public interface ProvincesEntityRepository extends JpaRepository<ProvincesEntity, Integer> {
    Optional<ProvincesEntity> findByCode(String code);

    @Query(value = """
            SELECT p.* FROM PROVINCES p 
            JOIN MERCHANT_PROVINCE mp ON p.ID = mp.PROVINCE_ID 
            WHERE mp.MERCHANT_ID = :merchantId
            """, nativeQuery = true)
    Page<ProvincesEntity> fetchByMerchantId(@Param("merchantId") String merchantId, Pageable pageable);

    @Query(value = """
            SELECT p.* FROM PROVINCES p 
            JOIN MERCHANT_PROVINCE mp ON p.ID = mp.PROVINCE_ID 
            WHERE mp.MERCHANT_ID = :merchantId 
            AND (:keyword IS NULL OR :keyword = '' OR LOWER(p.NAME) LIKE LOWER(CONCAT('%', :keyword, '%')))
            """, nativeQuery = true)
    Page<ProvincesEntity> searchByMerchantId(@Param("merchantId") String merchantId, @Param("keyword") String keyword, Pageable pageable);

    @Query(value = """
            SELECT  o.code AS originCode,
                    d.code AS destinationCode
            FROM PROVINCES o
            JOIN PROVINCES d
            ON d.name = :destination
            WHERE o.name = :origin
            """, nativeQuery = true)
    ProvincesCodeProjection selectProvincesCode(@Param("origin") String origin,
                                                @Param("destination") String destination);

}
