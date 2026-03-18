package vn.com.routex.hub.booking.service.application.services;


import vn.com.routex.hub.booking.service.controller.models.seat.GetAllSeatRequest;
import vn.com.routex.hub.booking.service.controller.models.seat.GetAllSeatResponse;
import vn.com.routex.hub.booking.service.controller.models.seat.HoldSeatRequest;
import vn.com.routex.hub.booking.service.controller.models.seat.HoldSeatResponse;

public interface RouteSeatManagementService {
    GetAllSeatResponse getAllSeat(GetAllSeatRequest request);

    HoldSeatResponse holdSeat(HoldSeatRequest request);
}
