package vn.com.routex.hub.booking.service.application.command.ticket;

import lombok.Builder;
import vn.com.routex.hub.booking.service.domain.ticket.TicketStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Builder
public record FetchTicketsResult(
        List<TicketItemResult> items,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages
) {
    @Builder
    public record TicketItemResult(
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
            OffsetDateTime issuedAt
    ) {
    }
}
