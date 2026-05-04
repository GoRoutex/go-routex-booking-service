package vn.com.routex.hub.booking.service.application.services;

import vn.com.routex.hub.booking.service.application.command.seat.HoldSeatCommand;
import vn.com.routex.hub.booking.service.application.command.seat.HoldSeatResult;

public interface HoldSeatService {

    HoldSeatResult holdSeat(HoldSeatCommand command);
}
