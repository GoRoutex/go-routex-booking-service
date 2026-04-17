package vn.com.routex.hub.booking.service.domain.merchant.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.booking.service.domain.auditing.AbstractAuditingEntity;
import vn.com.routex.hub.booking.service.domain.merchant.MerchantStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Merchant extends AbstractAuditingEntity {

    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");
    private String id;
    private String code;
    private String slug;
    private String displayName;
    private String legalName;
    private String taxCode;
    private String businessLicenseNumber;
    private String businessLicenseUrl;
    private String phone;
    private String email;
    private String logoUrl;
    private String description;
    private String address;
    private String ward;
    private String province;
    private String country;
    private String postalCode;
    private String representativeName;
    private String contactName;
    private String contactPhone;
    private String contactEmail;
    private String ownerFullName;
    private String ownerPhone;
    private String ownerEmail;
    private String bankAccountName;
    private String bankAccountNumber;
    private String bankName;
    private String bankBranch;
    private BigDecimal commissionRate;
    private MerchantStatus status;
    private OffsetDateTime approvedAt;
    private String approvedBy;

    public void updateCommissionRate(BigDecimal commissionRate, String actor, OffsetDateTime updatedAt) {
        this.commissionRate = commissionRate;
        this.setUpdatedBy(actor);
        this.setUpdatedAt(updatedAt);
    }

    public BigDecimal calculatePlatformCommissionAmount(BigDecimal ticketPrice) {
        validateTicketPrice(ticketPrice);
        return ticketPrice
                .multiply(getCommissionRateFraction())
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateMerchantProfitAmount(BigDecimal ticketPrice) {
        validateTicketPrice(ticketPrice);
        return ticketPrice
                .subtract(calculatePlatformCommissionAmount(ticketPrice))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateMerchantProfitRate() {
        return ONE_HUNDRED
                .subtract(normalizeCommissionRate())
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal getCommissionRateFraction() {
        return normalizeCommissionRate()
                .divide(ONE_HUNDRED, 4, RoundingMode.HALF_UP);
    }

    private BigDecimal normalizeCommissionRate() {
        return commissionRate == null ? BigDecimal.ZERO : commissionRate;
    }

    private void validateTicketPrice(BigDecimal ticketPrice) {
        if (ticketPrice == null || ticketPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("ticketPrice must be non-negative");
        }
    }
}
