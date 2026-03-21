package vn.com.routex.hub.booking.service.application.dto.booking;

import lombok.Builder;
import vn.com.routex.hub.booking.service.application.dto.common.RequestMetadata;

import java.time.OffsetDateTime;

@Builder
public record CreateBookingCommand(
        RequestMetadata metadata,
        String routeId,
        String vehicleId,
        String holdBy,
        String holdToken,
        OffsetDateTime heldAt,
        OffsetDateTime holdUntil,
        String customerId,
        String customerName,
        String customerPhone,
        String customerEmail,
        String currency
) {
}
