package vn.com.routex.hub.booking.service.domain.tripcontext.port;

import vn.com.routex.hub.booking.service.application.command.common.RequestContext;
import vn.com.routex.hub.booking.service.domain.tripcontext.model.TripBookingContext;

public interface TripBookingContextQueryPort {

    TripBookingContext fetchByTripId(String tripId, RequestContext context);
}
