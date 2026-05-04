package vn.com.routex.hub.booking.service.application.services;

import vn.com.routex.hub.booking.service.application.command.booking.CreateBookingCommand;
import vn.com.routex.hub.booking.service.domain.booking.model.Booking;
import vn.com.routex.hub.booking.service.domain.seat.model.TripSeat;

import java.util.List;

public interface BookingService {

    Booking createBooking(CreateBookingCommand command, List<TripSeat> tripSeatList);

}
