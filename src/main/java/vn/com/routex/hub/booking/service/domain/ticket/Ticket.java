package vn.com.routex.hub.booking.service.domain.ticket;


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

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "TICKET")
public class Ticket extends AbstractAuditingEntity {

    @Id
    private String id;
    @Column(name = "TICKET_CODE", nullable = false)
    private String ticketCode;
    @Column(name = "QR_CODE", nullable = false)
    private String qrCode;
    @Column(name = "CUSTOMER_PHONE", nullable = false)
    private String customerPhone;
    @Column(name = "CUSTOMER_EMAIL", nullable = false)
    private String customerEmail;
    @Column(name = "CUSTOMER_NAME", nullable = false)
    private String customerName;
    @Column(name = "ROUTE_ID", nullable = false)
    private String routeId;
    @Column(name = "BOOKING_ID", nullable = false)
    private String bookingId;
    @Column(name = "VEHICLE_ID", nullable = false)
    private String vehicleId;
    @Column(name = "SEAT_NUMBER", nullable = false)
    private String seatNumber;
    @Column(name = "SEAT_TYPE", nullable = false)
    private String seatType;
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private TicketStatus status;
    @Column(name = "CANCELLED_AT")
    private OffsetDateTime cancelledAt;

}
