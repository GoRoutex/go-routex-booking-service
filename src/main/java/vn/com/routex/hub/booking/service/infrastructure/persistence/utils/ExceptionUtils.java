package vn.com.routex.hub.booking.service.infrastructure.persistence.utils;


import lombok.experimental.UtilityClass;
import vn.com.routex.hub.booking.service.interfaces.models.result.ApiResult;

@UtilityClass
public class ExceptionUtils {

    public ApiResult buildResultResponse(String responseCode, String description) {
        return ApiResult
                .builder()
                .responseCode(responseCode)
                .description(description)
                .build();
    }
}
