package vn.com.routex.hub.booking.service.application.services;

import vn.com.routex.hub.booking.service.application.dto.seat.HoldSeatCommand;
import vn.com.routex.hub.booking.service.application.dto.seat.HoldSeatResult;

public interface HoldSeatService {

    HoldSeatResult holdSeat(HoldSeatCommand command);
}
