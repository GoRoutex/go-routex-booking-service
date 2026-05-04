package vn.com.routex.hub.booking.service.application.command.ticket;

import lombok.Builder;
import vn.com.routex.hub.booking.service.application.command.common.RequestContext;

@Builder
public record FetchTicketsQuery(
        RequestContext metadata,
        String customerId,
        String pageNumber,
        String pageSize
) {
}
