package vn.com.routex.hub.booking.service.application.command.ticket;

import lombok.Builder;
import vn.com.routex.hub.booking.service.domain.ticket.TicketStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Builder
public record FetchTicketDetailResult(
        String id,
        String ticketCode,
        String bookingId,
        String bookingSeatId,
        String tripId,
        String seatNumber,
        String customerName,
        String customerPhone,
        BigDecimal price,
        TicketStatus status,
        OffsetDateTime issuedAt,
        OffsetDateTime checkedInAt,
        OffsetDateTime boardedAt,
        OffsetDateTime cancelledAt,
        String checkedInBy,
        String boardedBy,
        String cancelledBy
) {
}
