package vn.com.routex.hub.booking.service.application.dto.seat;

import lombok.Builder;
import vn.com.routex.hub.booking.service.application.dto.common.RequestMetadata;

import java.util.List;

@Builder
public record HoldSeatCommand(
        RequestMetadata metadata,
        String routeId,
        String vehicleId,
        List<String> seatNos,
        String holdBy,
        String customerId,
        String customerName,
        String customerPhone,
        String customerEmail,
        String currency
) {
}
