package vn.com.routex.hub.booking.service.application.command.common;

import lombok.Builder;

@Builder
public record PageContext(
        String pageSize,
        String pageNumber
) {
}
