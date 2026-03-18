package vn.com.routex.hub.booking.service.controller.models.seat;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.booking.service.controller.models.base.BaseRequest;

import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ApplicationConstant.UUID_REGEX;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class GetAllSeatRequest extends BaseRequest {

    private GetAvailableSeatRequestData data;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class GetAvailableSeatRequestData {

        @NotNull
        @NotBlank
        @Pattern(regexp = UUID_REGEX, message = "must be in format of UUID (12)")
        private String routeId;
    }
}
