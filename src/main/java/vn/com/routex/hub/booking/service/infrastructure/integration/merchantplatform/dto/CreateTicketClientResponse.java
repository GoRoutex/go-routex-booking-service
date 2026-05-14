package vn.com.routex.hub.booking.service.infrastructure.integration.merchantplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.booking.service.interfaces.models.base.BaseResponse;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class CreateTicketClientResponse extends BaseResponse<List<CreateTicketClientResponse.CreateTicketClientResponseData>> {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class CreateTicketClientResponseData {
        private String ticketId;
        private String ticketCode;
        private String bookingSeatId;
        private String status;
    }
}
