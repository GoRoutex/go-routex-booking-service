package vn.com.routex.hub.booking.service.domain.payment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.booking.service.domain.auditing.AbstractAuditingEntity;
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
    private String bookingId;
    private String code;
    private String method;
    private BigDecimal amount;
    private String currency;
    private PaymentStatus status;
    private String checkoutUrl;
    private String paymentToken;
    private OffsetDateTime paidAt;
    private OffsetDateTime expiredAt;
    private OffsetDateTime failedAt;
    private String failureReason;
    private String description;
}

