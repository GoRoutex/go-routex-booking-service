package vn.com.routex.hub.booking.service.application.dto.common;

import lombok.Builder;

@Builder
public record RequestMetadata(
        String requestId,
        String requestDateTime,
        String channel
) {
}
