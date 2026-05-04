package vn.com.routex.hub.booking.service.application.command.booking;

import lombok.Builder;
import vn.com.routex.hub.booking.service.application.command.common.RequestContext;

import java.time.OffsetDateTime;

@Builder
public record CreateBookingCommand(
        RequestContext context,
        String merchantId,
        String tripId,
        String holdBy,
        String holdToken,
        OffsetDateTime heldAt,
        OffsetDateTime holdUntil,
        String customerId,
        String customerName,
        String customerPhone,
        String customerEmail
) {
}
