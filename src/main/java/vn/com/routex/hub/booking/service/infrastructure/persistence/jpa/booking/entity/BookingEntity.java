package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.booking.entity;


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
import vn.com.routex.hub.booking.service.domain.booking.BookingStatus;
import vn.com.routex.hub.booking.service.domain.booking.PaymentStatus;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.entity.AbstractAuditingEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "BOOKING")
public class BookingEntity extends AbstractAuditingEntity {
    @Id
    private String id;

    @Column(name = "BOOKING_CODE", nullable = false, unique = true)
    private String bookingCode;

    @Column(name = "TRIP_ID", nullable = false)
    private String tripId;

    @Column(name = "MERCHANT_ID", nullable = false)
    private String merchantId;

    @Column(name = "VEHICLE_ID", nullable = false)
    private String vehicleId;

    @Column(name = "CUSTOMER_ID")
    private String customerId;

    @Column(name = "CHANNEL", nullable = false)
    private String channel;

    @Column(name = "CUSTOMER_NAME")
    private String customerName;

    @Column(name = "CUSTOMER_PHONE")
    private String customerPhone;

    @Column(name = "CUSTOMER_EMAIL")
    private String customerEmail;

    @Column(name = "SEAT_COUNT")
    private Integer seatCount;

    @Column(name = "TOTAL_AMOUNT")
    private BigDecimal totalAmount;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Column(name = "PAYMENT_STATUS")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "PAYMENT_METHOD")
    private String paymentMethod;

    @Column(name = "PAYMENT_REFERENCE")
    private String paymentReference;

    @Column(name = "PAYMENT_TRANSACTION_ID")
    private String paymentTransactionId;

    @Column(name = "HELD_AT")
    private OffsetDateTime heldAt;

    @Column(name = "HOLD_UNTIL")
    private OffsetDateTime holdUntil;

    @Column(name = "HOLD_TOKEN")
    private String holdToken;

    @Column(name = "PAID_AT")
    private OffsetDateTime paidAt;

    @Column(name = "CANCELLED_AT")
    private OffsetDateTime cancelledAt;

    @Column(name = "CANCEL_REASON")
    private String cancelReason;

    @Column(name = "NOTE")
    private String note;

    @Column(name = "CREATOR")
    private String creator;


}
