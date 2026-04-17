package vn.com.routex.hub.booking.service.domain.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.booking.service.domain.auditing.AbstractAuditingEntity;
import vn.com.routex.hub.booking.service.domain.review.ReviewType;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class MerchantReview extends AbstractAuditingEntity {
    private String id;
    private String merchantId;
    private ReviewType reviewType;
    private String bookingId;
    private String routeId;
    private String routeCode;
    private String driverId;
    private String vehicleId;
    private String customerId;
    private String customerName;
    private Integer overallRating;
    private Integer driverRating;
    private Integer vehicleRating;
    private Integer punctualityRating;
    private Integer tripExperienceRating;
    private Integer safetyRating;
    private Integer merchantServiceRating;
    private Integer staffSupportRating;
    private Integer valueForMoneyRating;
    private String comment;
    private OffsetDateTime reviewedAt;

    public static MerchantReview create(
            String id,
            String merchantId,
            ReviewType reviewType,
            String bookingId,
            String routeId,
            String routeCode,
            String driverId,
            String vehicleId,
            String customerId,
            String customerName,
            Integer overallRating,
            Integer driverRating,
            Integer vehicleRating,
            Integer punctualityRating,
            Integer tripExperienceRating,
            Integer safetyRating,
            Integer merchantServiceRating,
            Integer staffSupportRating,
            Integer valueForMoneyRating,
            String comment,
            OffsetDateTime reviewedAt,
            String createdBy
    ) {
        OffsetDateTime now = OffsetDateTime.now();
        return MerchantReview.builder()
                .id(id)
                .merchantId(merchantId)
                .reviewType(reviewType)
                .bookingId(bookingId)
                .routeId(routeId)
                .routeCode(routeCode)
                .driverId(driverId)
                .vehicleId(vehicleId)
                .customerId(customerId)
                .customerName(customerName)
                .overallRating(overallRating)
                .driverRating(driverRating)
                .vehicleRating(vehicleRating)
                .punctualityRating(punctualityRating)
                .tripExperienceRating(tripExperienceRating)
                .safetyRating(safetyRating)
                .merchantServiceRating(merchantServiceRating)
                .staffSupportRating(staffSupportRating)
                .valueForMoneyRating(valueForMoneyRating)
                .comment(comment)
                .reviewedAt(reviewedAt == null ? now : reviewedAt)
                .createdAt(now)
                .createdBy(createdBy)
                .build();
    }
}
