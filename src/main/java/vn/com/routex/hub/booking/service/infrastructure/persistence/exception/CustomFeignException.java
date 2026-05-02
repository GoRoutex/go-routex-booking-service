package vn.com.routex.hub.booking.service.infrastructure.persistence.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.com.routex.hub.booking.service.interfaces.models.result.ApiResult;

@Getter
@EqualsAndHashCode(callSuper = true)
public class CustomFeignException extends BaseException {

    private final String targetService;
    private final Integer httpStatus;
    private final String errorMessage;

    public CustomFeignException(
            String requestId,
            String requestDateTime,
            String channel,
            ApiResult result,
            String targetService,
            Integer httpStatus,
            String errorMessage,
            Throwable cause
    ) {
        super(requestId, requestDateTime, channel, result);
        this.targetService = targetService;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
        if (cause != null) {
            initCause(cause);
        }
    }
}
