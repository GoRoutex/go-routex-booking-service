package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.booking.service.domain.review.ReviewType;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.review.entity.MerchantReviewEntity;

@Repository
public interface MerchantReviewEntityRepository extends JpaRepository<MerchantReviewEntity, String> {

    Page<MerchantReviewEntity> findByMerchantIdOrderByReviewedAtDesc(String merchantId, Pageable pageable);

    long countByMerchantId(String merchantId);

    boolean existsByMerchantIdAndBookingIdAndCustomerIdAndReviewType(
            String merchantId,
            String bookingId,
            String customerId,
            ReviewType reviewType
    );

    @Query("select avg(r.overallRating) from MerchantReviewEntity r where r.merchantId = :merchantId")
    Double findAverageOverallRatingByMerchantId(@Param("merchantId") String merchantId);
}
