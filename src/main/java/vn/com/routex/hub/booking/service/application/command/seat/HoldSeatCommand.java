package vn.com.routex.hub.booking.service.application.command.seat;

import lombok.Builder;
import vn.com.routex.hub.booking.service.application.command.common.RequestContext;

import java.util.List;

@Builder
public record HoldSeatCommand(
        RequestContext context,
        String routeId,
        List<String> seatNos,
        String holdBy,
        String customerName,
        String customerPhone,
        String customerEmail
) {
}
