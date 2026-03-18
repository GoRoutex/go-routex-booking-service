package vn.com.routex.hub.booking.service.controller.models.seat;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.booking.service.controller.models.base.BaseRequest;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class HoldSeatRequest extends BaseRequest {

    @Valid
    @NotNull
    private HoldSeatRequestData data;

    @Valid
    @NotNull
    private HoldSeatRequestInformation info;


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class HoldSeatRequestInformation {
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
    public static class HoldSeatRequestData {
        @NotBlank
        @NotNull
        private String routeId;

        @NotBlank
        @NotNull
        private String vehicleId;

        @NotEmpty
        private List<String> seatNos;

        private String holdBy;
    }
}
