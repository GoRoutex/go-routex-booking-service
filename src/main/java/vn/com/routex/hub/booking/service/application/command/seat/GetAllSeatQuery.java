package vn.com.routex.hub.booking.service.application.command.seat;

import lombok.Builder;
import vn.com.routex.hub.booking.service.application.command.common.RequestContext;

@Builder
public record GetAllSeatQuery(
        RequestContext metadata,
        String routeId
) {
}
