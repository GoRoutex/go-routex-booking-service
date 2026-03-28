package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.ticket.entity;

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
import vn.com.routex.hub.booking.service.domain.ticket.TicketStatus;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.entity.AuditingEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "TICKET")
public class TicketEntity extends AuditingEntity {

    @Id
    private String id;

    @Column(name = "TICKET_CODE")
    private String ticketCode;

    @Column(name = "BOOKING_ID")
    private String bookingId;

    @Column(name = "BOOKING_SEAT_ID")
    private String bookingSeatId;

    @Column(name = "ROUTE_ID")
    private String routeId;

    @Column(name = "SEAT_NUMBER")
    private String seatNumber;

    @Column(name = "CUSTOMER_NAME")
    private String customerName;

    @Column(name = "CUSTOMER_PHONE")
    private String customerPhone;

    @Column(name = "PRICE")
    private BigDecimal price;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @Column(name = "ISSUED_AT")
    private OffsetDateTime issuedAt;

    @Column(name = "CHECKED_IN_AT")
    private OffsetDateTime checkedInAt;

    private OffsetDateTime boardedAt;

    @Column(name = "CANCELLED_AT")
    private OffsetDateTime cancelledAt;

    @Column(name = "CHECKED_IN_BY")
    private String checkedInBy;

    @Column(name = "BOARDED_BY")
    private String boardedBy;

    @Column(name = "CANCELLED_BY")
    private String cancelledBy;
}
