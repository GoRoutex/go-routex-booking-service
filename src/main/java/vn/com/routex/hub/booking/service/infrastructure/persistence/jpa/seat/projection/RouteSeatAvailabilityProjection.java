package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.seat.projection;

public interface RouteSeatAvailabilityProjection {

    String getRouteId();

    Long getAvailableSeat();
}

