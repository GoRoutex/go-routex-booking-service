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
    private ApplicationFormStatus status;
    private OffsetDateTime submittedAt;
    private String submittedBy;
    private String taxCode;
    private String slug;

    public void approve(String merchantId, String approvedBy, OffsetDateTime approvedAt) {
        this.merchantId = merchantId;
        this.approvedBy = approvedBy;
        this.approvedAt = approvedAt;
        this.rejectedBy = null;
        this.rejectionReason = null;
        this.status = ApplicationFormStatus.APPROVED;
        this.setUpdatedAt(approvedAt);
        this.setUpdatedBy(approvedBy);
    }

    public void reject(String rejectedBy, String rejectionReason, OffsetDateTime rejectedAt) {
        this.rejectedBy = rejectedBy;
        this.rejectionReason = rejectionReason;
        this.approvedBy = null;
        this.approvedAt = null;
        this.status = ApplicationFormStatus.REJECTED;
        this.setUpdatedAt(rejectedAt);
        this.setUpdatedBy(rejectedBy);
    }

    public static MerchantApplicationForm submit(
            String id,
            String formCode,
            String displayName,
            String legalName,
            String taxCode,
            String businessLicense,
            String businessLicenseUrl,
            String country,
            String province,
            String district,
            String city,
            String postalCode,
            String description,
            String slug,
            String contactName,
            String contactPhone,
            String contactEmail,
            String bankName,
            String bankBranch,
            String bankAccountName,
            String bankAccountNumber,
            String ownerName,
            String ownerFullName,
            String ownerPhone,
            String ownerEmail
    ) {
        OffsetDateTime submittedAt = OffsetDateTime.now();
        return MerchantApplicationForm.builder()
                .id(id)
                .formCode(formCode)
                .displayName(displayName)
                .legalName(legalName)
                .taxCode(taxCode)
                .businessLicense(businessLicense)
                .businessLicenseUrl(businessLicenseUrl)
                .country(country)
                .province(province)
                .district(district)
                .city(city)
                .postalCode(postalCode)
                .description(description)
                .slug(slug)
                .merchantName(displayName)
                .contact(new ApplicationFormContact(contactEmail, contactName, contactPhone))
                .bankInfo(new ApplicationFormBankInfo(bankAccountName, bankAccountNumber, bankBranch, bankName))
                .ownerInfo(new ApplicationFormOwner(ownerEmail, ownerFullName, ownerName, ownerPhone))
                .status(ApplicationFormStatus.SUBMITTED)
                .submittedAt(submittedAt)
                .submittedBy(contactEmail)
                .createdAt(submittedAt)
                .createdBy(contactEmail)
                .build();
    }
}
