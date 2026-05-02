package vn.com.routex.hub.booking.service.infrastructure.integration.payment.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.booking.service.interfaces.models.base.BaseRequest;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FetchPaymentContextClientRequest extends BaseRequest {

    @Valid
    @NotNull
    private FetchPaymentContextClientRequestData data;

    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FetchPaymentContextClientRequestData {
        private String bookingCode;
    }
}
