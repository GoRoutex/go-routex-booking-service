package vn.com.routex.hub.booking.service.infrastructure.kafka.event;

import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.List;

@Builder
public record RouteReadyForSaleEvent(
        String routeId,
        String vehicleId,
        String assignedBy,
        OffsetDateTime assignedAt,
        String routeStatus,
        Integer seatCount,
        List<String> seatNos
) {
}
