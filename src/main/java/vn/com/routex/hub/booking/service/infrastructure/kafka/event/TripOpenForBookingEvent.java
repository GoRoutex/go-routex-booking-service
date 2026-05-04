package vn.com.routex.hub.booking.service.infrastructure.kafka.event;

import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record TripOpenForBookingEvent(
        String tripId,
        String vehicleId,
        Long seatCount,
        String creator,
        OffsetDateTime assignedAt
) {
}
