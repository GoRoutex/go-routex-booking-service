package vn.com.routex.hub.booking.service.interfaces.models.result;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_CODE;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_MESSAGE;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResult {
    private String responseCode;
    private String description;

    public static ApiResult buildSuccess() {
        return ApiResult.builder()
                .responseCode(SUCCESS_CODE)
                .description(SUCCESS_MESSAGE)
                .build();
    }
}
