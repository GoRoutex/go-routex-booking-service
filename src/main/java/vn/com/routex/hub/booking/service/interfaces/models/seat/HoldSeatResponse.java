package vn.com.routex.hub.booking.service.interfaces.models.seat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.booking.service.interfaces.models.base.BaseResponse;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class HoldSeatResponse extends BaseResponse<List<HoldSeatResponse.HoldSeatResponseData>> {
    @Getter
    @Setter
    @NoArgsConstructor
    @SuperBuilder
    public static class HoldSeatResponseData {
        private String routeId;
        private String seatNo;
        private String status;
        private OffsetDateTime holdUntil;
        private String holdBy;
    }
}
