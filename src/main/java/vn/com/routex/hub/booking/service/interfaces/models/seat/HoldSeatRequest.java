package vn.com.routex.hub.booking.service.interfaces.models.seat;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.booking.service.interfaces.models.base.BaseRequest;

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
        private String customerName;
        private String customerPhone;
        private String customerEmail;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class HoldSeatRequestData {
        @NotBlank
        @NotNull
        private String tripId;

        @NotEmpty
        private List<String> seatNos;

        private String holdBy;
    }
}
