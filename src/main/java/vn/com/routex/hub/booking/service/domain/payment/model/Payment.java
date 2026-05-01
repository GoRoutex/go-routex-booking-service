package vn.com.routex.hub.booking.service.domain.payment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.booking.service.domain.auditing.AbstractAuditingEntity;
import vn.com.routex.hub.booking.service.domain.payment.PaymentMethod;
import vn.com.routex.hub.booking.service.domain.payment.PaymentStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Domain model (no JPA annotations).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Payment extends AbstractAuditingEntity {

    private String id;
    private String bookingCode;
    private PaymentMethod method;
    private String txnRef;
    private BigDecimal amount;
    private String currency;
    private PaymentStatus status;
    private OffsetDateTime paidAt;
    private OffsetDateTime failedAt;
    private String failureReason;
    private String description;

    public boolean isReusablePendingPayment(OffsetDateTime now) {
        return PaymentStatus.PENDING.equals(status);
    }

    public void markPaid(OffsetDateTime now) {
        paidAt = now;
        status = PaymentStatus.PAID;
        this.setUpdatedAt(now);
        failureReason = null;
    }

    public void markFailed(OffsetDateTime now, String reason) {
        status = PaymentStatus.FAILED;
        failedAt = now;
        this.setUpdatedAt(now);
        failureReason = reason;
    }
}

