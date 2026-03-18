package vn.com.routex.hub.booking.service.application.services;

import vn.com.routex.hub.booking.service.domain.booking.Booking;
import vn.com.routex.hub.booking.service.domain.seat.RouteSeat;
import vn.com.routex.hub.booking.service.controller.models.booking.CreateBookingRequest;

import java.util.List;

public interface BookingService {

    Booking createBooking(CreateBookingRequest request, List<RouteSeat> routeSeatList);

}
