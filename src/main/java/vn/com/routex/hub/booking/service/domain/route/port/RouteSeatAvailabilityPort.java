package vn.com.routex.hub.booking.service.domain.route.port;

import java.util.List;
import java.util.Map;

public interface RouteSeatAvailabilityPort {
    Map<String, Long> countAvailableSeats(List<String> routeIds);
}
