package vn.com.routex.hub.booking.service.domain.route.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class VehicleSnapshot {
    private String id;
    private String vehiclePlate;
    private Integer seatCapacity;
    private boolean hasFloor;
}
