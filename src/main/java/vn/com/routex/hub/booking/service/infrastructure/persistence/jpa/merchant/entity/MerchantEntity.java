package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.merchant.entity;


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
import vn.com.routex.hub.booking.service.domain.merchant.MerchantStatus;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.entity.AbstractAuditingEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "MERCHANTS")
public class MerchantEntity extends AbstractAuditingEntity {

    @Id
    private String id;

    @Column(name = "CODE", nullable = false, unique = true)
    private String code;

    @Column(name = "SLUG", unique = true)
    private String slug;

    @Column(name = "DISPLAY_NAME", nullable = false)
    private String displayName;

    @Column(name = "LEGAL_NAME")
    private String legalName;

    @Column(name = "TAX_CODE")
    private String taxCode;

    @Column(name = "BUSINESS_LICENSE_NUMBER")
    private String businessLicenseNumber;

    @Column(name = "BUSINESS_LICENSE_URL")
    private String businessLicenseUrl;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "LOGO_URL")
    private String logoUrl;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "WARD")
    private String ward;

    @Column(name = "PROVINCE")
    private String province;

    @Column(name = "COUNTRY")
    private String country;

    @Column(name = "POSTAL_CODE")
    private String postalCode;

    @Column(name = "REPRESENTATIVE_NAME")
    private String representativeName;

    @Column(name = "CONTACT_NAME")
    private String contactName;

    @Column(name = "CONTACT_PHONE")
    private String contactPhone;

    @Column(name = "CONTACT_EMAIL")
    private String contactEmail;

    @Column(name = "OWNER_FULL_NAME")
    private String ownerFullName;

    @Column(name = "OWNER_PHONE")
    private String ownerPhone;

    @Column(name = "OWNER_EMAIL")
    private String ownerEmail;

    @Column(name = "BANK_ACCOUNT_NAME")
    private String bankAccountName;

    @Column(name = "BANK_ACCOUNT_NUMBER")
    private String bankAccountNumber;

    @Column(name = "BANK_NAME")
    private String bankName;

    @Column(name = "BANK_BRANCH")
    private String bankBranch;

    @Column(name = "COMMISSION_RATE", precision = 5, scale = 2)
    private BigDecimal commissionRate;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private MerchantStatus status;

    @Column(name = "APPROVED_AT")
    private OffsetDateTime approvedAt;

    @Column(name = "APPROVED_BY")
    private String approvedBy;
}