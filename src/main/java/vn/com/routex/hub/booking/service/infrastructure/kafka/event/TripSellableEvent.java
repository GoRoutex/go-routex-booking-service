package vn.com.routex.hub.booking.service.infrastructure.kafka.event;

import lombok.Builder;
import vn.com.routex.hub.booking.service.domain.trip.TripStatus;

import java.time.OffsetDateTime;

@Builder
public record TripSellableEvent(
        String tripId,
        String vehicleId,
        String assignedBy,
        OffsetDateTime assignedAt,
        TripStatus status,
        Long seatCount,
        String creator,
        Boolean hasFloor
) {
}
