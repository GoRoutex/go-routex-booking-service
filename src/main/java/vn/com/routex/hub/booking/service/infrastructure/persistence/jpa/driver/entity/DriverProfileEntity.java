package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.driver.entity;

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
import vn.com.routex.hub.booking.service.domain.driver.DriverStatus;
import vn.com.routex.hub.booking.service.domain.driver.OperationStatus;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.entity.AbstractAuditingEntity;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@SuperBuilder
@Table(name = "DRIVER_PROFILE")
public class DriverProfileEntity extends AbstractAuditingEntity {

    @Id
    private String id;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "MERCHANT_ID")
    private String merchantId;

    @Column(name = "EMPLOYEE_CODE")
    private String employeeCode;

    @Column(name = "EMERGENCY_CONTACT_NAME")
    private String emergencyContactName;

    @Column(name = "EMERGENCY_CONTACT_PHONE")
    private String emergencyContactPhone;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private DriverStatus status;

    @Column(name = "APPROVAL_STATUS")
    private String approvalStatus;

    @Column(name = "APPROVED_BY")
    private String approvedBy;

    @Column(name = "APPROVED_AT")
    private OffsetDateTime approvedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "OPERATION_STATUS", nullable = false)
    private OperationStatus operationStatus;

    @Column(name = "RATING")
    private Double rating;

    @Column(name = "TOTAL_TRIPS")
    private Integer totalTrips;

    @Column(name = "LICENSE_CLASS")
    private String licenseClass;

    @Column(name = "LICENSE_NUMBER")
    private String licenseNumber;

    @Column(name = "LICENSE_ISSUE_DATE")
    private LocalDate licenseIssueDate;

    @Column(name = "LICENSE_EXPIRY_DATE")
    private LocalDate licenseExpiryDate;

    @Column(name = "POINTS_DELTA")
    private Integer pointsDelta;

    @Column(name = "POINTS_REASON")
    private String pointsReason;

    @Column(name = "KYC_VERIFIED")
    private Boolean kycVerified;

    @Column(name = "TRAINING_COMPLETED")
    private Boolean trainingCompleted;

    @Column(name = "NOTE")
    private String note;
}
