package vn.com.routex.hub.booking.service.domain.merchant.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.booking.service.domain.auditing.AbstractAuditingEntity;
import vn.com.routex.hub.booking.service.domain.merchant.ApplicationFormBankInfo;
import vn.com.routex.hub.booking.service.domain.merchant.ApplicationFormContact;
import vn.com.routex.hub.booking.service.domain.merchant.ApplicationFormOwner;
import vn.com.routex.hub.booking.service.domain.merchant.ApplicationFormStatus;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class MerchantApplicationForm extends AbstractAuditingEntity {

    private String id;
    private String displayName;
    private String legalName;
    private ApplicationFormContact contact;
    private ApplicationFormBankInfo bankInfo;
    private ApplicationFormOwner ownerInfo;
    private String approvedBy;
    private OffsetDateTime approvedAt;
    private String logoUrl;
    private String businessLicenseUrl;
    private String businessLicense;
    private String city;
    private String country;
    private String description;
    private String district;
    private String formCode;
    private String postalCode;
    private String province;
    private String rejectedBy;
    private String rejectionReason;
    private ApplicationFormStatus status;
    private OffsetDateTime submittedAt;
    private String submittedBy;
    private String taxCode;
    private String slug;
}
