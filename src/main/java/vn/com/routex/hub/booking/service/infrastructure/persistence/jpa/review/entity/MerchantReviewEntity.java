package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.review.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.booking.service.domain.review.ReviewType;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.entity.AbstractAuditingEntity;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "MERCHANT_REVIEW")
public class MerchantReviewEntity extends AbstractAuditingEntity {

    @Id
    private String id;

    @Column(name = "MERCHANT_ID", nullable = false)
    private String merchantId;

    @Column(name = "REVIEW_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private ReviewType reviewType;

    @Column(name = "BOOKING_ID")
    private String bookingId;

    @Column(name = "ROUTE_ID")
    private String routeId;

    @Column(name = "ROUTE_CODE")
    private String routeCode;

    @Column(name = "DRIVER_ID")
    private String driverId;

    @Column(name = "VEHICLE_ID")
    private String vehicleId;

    @Column(name = "CUSTOMER_ID")
    private String customerId;

    @Column(name = "CUSTOMER_NAME")
    private String customerName;

    @Column(name = "OVERALL_RATING", nullable = false)
    private Integer overallRating;

    @Column(name = "DRIVER_RATING")
    private Integer driverRating;

    @Column(name = "VEHICLE_RATING")
    private Integer vehicleRating;

    @Column(name = "PUNCTUALITY_RATING")
    private Integer punctualityRating;

    @Column(name = "TRIP_EXPERIENCE_RATING")
    private Integer tripExperienceRating;

    @Column(name = "SAFETY_RATING")
    private Integer safetyRating;

    @Column(name = "MERCHANT_SERVICE_RATING")
    private Integer merchantServiceRating;

    @Column(name = "STAFF_SUPPORT_RATING")
    private Integer staffSupportRating;

    @Column(name = "VALUE_FOR_MONEY_RATING")
    private Integer valueForMoneyRating;

    @Column(name = "COMMENT", length = 2000)
    private String comment;

    @Column(name = "REVIEWED_AT", nullable = false)
    private OffsetDateTime reviewedAt;
}
