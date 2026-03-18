package vn.com.routex.hub.booking.service.controller.models.booking;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.booking.service.controller.models.base.BaseRequest;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CreateBookingRequest extends BaseRequest {

    private CreateBookingRequestData data;
    private CreateBookingRequestInformation info;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class CreateBookingRequestInformation {
        private String customerId;
        private String customerName;
        private String customerPhone;
        private String customerEmail;
        private String currency;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class CreateBookingRequestData {
        private String routeId;
        private String vehicleId;
        private String holdBy;
        private String holdToken;
        private OffsetDateTime heldAt;
        private OffsetDateTime holdUntil;
    }
}
