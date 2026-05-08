package vn.com.routex.hub.booking.service.infrastructure.integration.merchantplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.booking.service.interfaces.models.base.BaseResponse;

import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class FetchTripBookingContextClientResponse
        extends BaseResponse<FetchTripBookingContextClientResponse.FetchTripBookingContextClientResponseData> {

    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FetchTripBookingContextClientResponseData {
        private String tripId;
        private String routeId;
        private String merchantId;
        private String vehicleId;
        private BigDecimal ticketPrice;
        private String pickupBranch;
        private String originName;
        private String destinationName;
        private String routeStatus;
        private String tripStatus;
    }
}
