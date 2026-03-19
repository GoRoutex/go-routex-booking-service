package vn.com.routex.hub.booking.service.application.services;


import vn.com.routex.hub.booking.service.controller.models.seat.GetAllSeatRequest;
import vn.com.routex.hub.booking.service.controller.models.seat.GetAllSeatResponse;

public interface GetAllSeatService {
    GetAllSeatResponse getAllSeat(GetAllSeatRequest request);
}
