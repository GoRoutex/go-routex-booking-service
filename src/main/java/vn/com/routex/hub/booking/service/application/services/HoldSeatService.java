package vn.com.routex.hub.booking.service.application.services;

import vn.com.routex.hub.booking.service.controller.models.seat.HoldSeatRequest;
import vn.com.routex.hub.booking.service.controller.models.seat.HoldSeatResponse;

public interface HoldSeatService {

    HoldSeatResponse holdSeat(HoldSeatRequest request);
}
