package vn.com.routex.hub.booking.service.domain.vehicle.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.com.routex.hub.booking.service.domain.seat.SeatFloor;
import vn.com.routex.hub.booking.service.domain.vehicle.VehicleStatus;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleSeatBlueprint {
    private String vehicleId;
    private String merchantId;
    private String templateId;
    private Long seatCapacity;
    private boolean hasFloor;
    private VehicleStatus vehicleStatus;
    private List<SeatBlueprintItem> seats;

    @Getter
    @Setter
    @Builder
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
