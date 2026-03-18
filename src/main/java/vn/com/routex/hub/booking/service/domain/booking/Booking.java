package vn.com.routex.hub.booking.service.domain.booking;


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
import vn.com.routex.hub.booking.service.domain.auditing.AbstractAuditingEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "BOOKING")
public class Booking extends AbstractAuditingEntity {
    @Id
    private String id;

    @Column(name = "BOOKING_CODE", nullable = false, unique = true)
    private String bookingCode;

    @Column(name = "ROUTE_ID", nullable = false)
    private String routeId;

    @Column(name = "CUSTOMER_ID", nullable = false)
    private String customerId;

    @Column(name = "SEAT_COUNT")
    private Integer seatCount;

    @Column(name = "TOTAL_AMOUNT")
    private BigDecimal totalAmount;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Column(name = "HELD_AT")
    private OffsetDateTime heldAt;

    @Column(name = "HOLD_UNTIL")
    private OffsetDateTime holdUntil;

    @Column(name = "CANCELLED_AT")
    private OffsetDateTime cancelledAt;

    @Column(name = "NOTE")
    private String note;

    @Column(name = "CREATOR")
    private String creator;
}
