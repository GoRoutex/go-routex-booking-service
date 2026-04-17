package vn.com.routex.hub.booking.service.infrastructure.kafka.event;

import lombok.Builder;
import vn.com.routex.hub.booking.service.domain.ticket.TicketStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Builder
public record TicketIssuedEvent(
        String bookingId,
        String bookingCode,
        String customerId,
        String customerName,
        String customerPhone,
        String customerEmail,
        String routeId,
        BigDecimal totalAmount,
        String currency,
        OffsetDateTime paidAt,
        List<TicketIssuedItem> tickets
) {
    @Builder
    public record TicketIssuedItem(
            String ticketId,
            String ticketCode,
            String bookingSeatId,
            String seatNumber,
            BigDecimal price,
            TicketStatus status,
            OffsetDateTime issuedAt
    ) {
    }
}
