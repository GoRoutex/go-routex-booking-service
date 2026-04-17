package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.merchant.entity;


import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import vn.com.routex.hub.booking.service.domain.merchant.ApplicationFormBankInfo;
import vn.com.routex.hub.booking.service.domain.merchant.ApplicationFormContact;
import vn.com.routex.hub.booking.service.domain.merchant.ApplicationFormOwner;
import vn.com.routex.hub.booking.service.domain.merchant.ApplicationFormStatus;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.entity.AbstractAuditingEntity;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "MERCHANT_APPLICATION_FORM")
public class MerchantApplicationFormEntity extends AbstractAuditingEntity {

    @Id
    private String id;
    private String displayName;
    private String legalName;
    @Embedded
    private ApplicationFormContact contact;
    @Embedded
    private ApplicationFormBankInfo bankInfo;
    @Embedded
    private ApplicationFormOwner ownerInfo;
    private String approvedBy;
    private OffsetDateTime approvedAt;
    private String businessLicenseUrl;
    private String businessLicense;
    private String city;
    private String country;
    private String description;
    private String district;
    private String formCode;
    private String merchantId;
    private String merchantName;
    private String postalCode;
    private String province;
    private String rejectedBy;
    private String rejectionReason;
    @Enumerated(EnumType.STRING)
    private ApplicationFormStatus status;
    private OffsetDateTime submittedAt;
    private String submittedBy;
    private String taxCode;
    private String slug;
}
