package vn.com.routex.hub.booking.service.application.dto.seat;

import lombok.Builder;
import vn.com.routex.hub.booking.service.application.dto.common.RequestMetadata;

@Builder
public record GetAllSeatQuery(
        RequestMetadata metadata,
        String routeId
) {
}
