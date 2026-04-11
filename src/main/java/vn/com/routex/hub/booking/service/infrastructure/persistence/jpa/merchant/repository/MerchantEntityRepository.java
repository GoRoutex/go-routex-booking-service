package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.merchant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.merchant.entity.MerchantEntity;

public interface MerchantEntityRepository extends JpaRepository<MerchantEntity, String> {

    boolean existsByCode(String code);

    @Query(value = """
            SELECT generate_merchant_code()
            """, nativeQuery = true)
    String generateMerchantcode();

}
