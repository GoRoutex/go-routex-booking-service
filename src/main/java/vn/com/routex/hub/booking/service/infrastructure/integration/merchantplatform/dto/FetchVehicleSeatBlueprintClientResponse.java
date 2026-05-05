package vn.com.routex.hub.booking.service.infrastructure.integration.merchantplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.booking.service.domain.seat.SeatFloor;
import vn.com.routex.hub.booking.service.domain.vehicle.VehicleStatus;
import vn.com.routex.hub.booking.service.interfaces.models.base.BaseResponse;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class FetchVehicleSeatBlueprintClientResponse
        extends BaseResponse<FetchVehicleSeatBlueprintClientResponse.FetchVehicleSeatBlueprintClientResponseData> {

    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FetchVehicleSeatBlueprintClientResponseData {
        private String vehicleId;
        private String merchantId;
        private String templateId;
        private Long seatCapacity;
        private boolean hasFloor;
        private VehicleStatus vehicleStatus;
        private List<SeatBlueprintItem> seats;
    }

    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SeatBlueprintItem {
        private String id;
        private String seatCode;
        private SeatFloor floor;
        private int rowNo;
        private int columnNo;
    }
}
