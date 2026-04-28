package vn.com.routex.hub.booking.service.application.command.common;

import lombok.Builder;

@Builder
public record RequestContext(
        String requestId,
        String requestDateTime,
        String channel,
        String merchantId
) {
}
