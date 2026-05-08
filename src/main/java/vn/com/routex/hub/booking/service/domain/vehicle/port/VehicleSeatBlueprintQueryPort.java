package vn.com.routex.hub.booking.service.domain.vehicle.port;

import vn.com.routex.hub.booking.service.application.command.common.RequestContext;
import vn.com.routex.hub.booking.service.domain.vehicle.model.VehicleSeatBlueprint;

public interface VehicleSeatBlueprintQueryPort {

    VehicleSeatBlueprint fetchByVehicleId(String vehicleId, RequestContext context);
}
