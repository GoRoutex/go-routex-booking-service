package vn.com.routex.hub.booking.service.application.dto.ticket;

import lombok.Builder;
import vn.com.routex.hub.booking.service.application.dto.common.RequestMetadata;

@Builder
public record FetchTicketsQuery(
        RequestMetadata metadata,
        String customerId,
        String pageNumber,
        String pageSize
) {
}
