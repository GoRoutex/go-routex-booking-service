package vn.com.routex.hub.booking.service.domain.vehicle.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.com.routex.hub.booking.service.domain.vehicle.VehicleStatus;
import vn.com.routex.hub.booking.service.domain.vehicle.VehicleType;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleProfile {
    private String id;
    private String creator;
    private VehicleStatus status;
    private VehicleType type;
    private String vehiclePlate;
    private Integer seatCapacity;
    private boolean hasFloor;
    private String manufacturer;
}
