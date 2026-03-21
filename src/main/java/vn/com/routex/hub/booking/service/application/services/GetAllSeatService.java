package vn.com.routex.hub.booking.service.application.services;


import vn.com.routex.hub.booking.service.application.dto.seat.GetAllSeatQuery;
import vn.com.routex.hub.booking.service.application.dto.seat.GetAllSeatResult;

public interface GetAllSeatService {
    GetAllSeatResult getAllSeat(GetAllSeatQuery query);
}
