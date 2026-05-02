package vn.com.routex.hub.booking.service.application.services;

import vn.com.routex.hub.booking.service.application.command.common.RequestContext;
import vn.com.routex.hub.booking.service.domain.booking.model.Booking;

public interface BookingPaymentQueryService {

    Booking getBookingPaymentContext(String bookingCode, RequestContext context);
}
