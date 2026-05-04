package vn.com.routex.hub.booking.service.infrastructure.kafka.event;

import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record TripSellableEvent(
        String tripId,
        String vehicleId,
        String assignedBy,
        OffsetDateTime assignedAt,
        String routeStatus,
        Long seatCount,
        String creator,
        Boolean hasFloor
) {
}
