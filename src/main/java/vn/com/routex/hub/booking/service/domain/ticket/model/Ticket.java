package vn.com.routex.hub.booking.service.domain.ticket.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.booking.service.domain.auditing.AbstractAuditingEntity;
import vn.com.routex.hub.booking.service.domain.ticket.TicketStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Ticket extends AbstractAuditingEntity {
    private String id;
    private String ticketCode;
    private String bookingId;
    private String bookingSeatId;
    private String routeId;
    private String vehicleId;
    private String seatNumber;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private BigDecimal price;
    private TicketStatus status;
    private OffsetDateTime issuedAt;
    private OffsetDateTime checkedInAt;
    private OffsetDateTime boardedAt;
    private OffsetDateTime cancelledAt;
    private String checkedInBy;
    private String boardedBy;
    private String cancelledBy;
}
