package vn.com.routex.hub.booking.service.controller.models.ticket;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.booking.service.controller.models.base.BaseRequest;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class FetchTicketDetailRequest extends BaseRequest {

    @Valid
    @NotNull
    private FetchTicketDetailRequestData data;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class FetchTicketDetailRequestData {
        @NotBlank
        private String customerId;

        @NotBlank
        private String ticketId;
    }
}
