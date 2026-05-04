package vn.com.routex.hub.booking.service.infrastructure.integration.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.booking.service.interfaces.models.base.BaseResponse;

import java.time.OffsetDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class FetchPaymentContextClientResponse extends BaseResponse<FetchPaymentContextClientResponse.FetchPaymentContextClientResponseData> {

    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FetchPaymentContextClientResponseData {
        private String paymentId;
        private String bookingCode;
        private String paymentStatus;
        private OffsetDateTime paidAt;
    }
}
