package vn.com.routex.hub.booking.service.domain.seat.port;

import vn.com.routex.hub.booking.service.domain.seat.model.RouteSeat;

import java.util.List;
import java.util.Optional;

public interface RouteSeatRepositoryPort {
    boolean existsByRouteId(String routeId);

    List<RouteSeat> findAllByRouteIdOrderBySeatNoAsc(String routeId);

    List<RouteSeat> findAllByRouteIdAndSeatNoInForUpdate(String routeId, List<String> seatNos);

    Optional<RouteSeat> findByRouteIdAndSeatNo(String routeId, String seatNo);

    List<RouteSeat> saveAll(List<RouteSeat> routeSeats);

    RouteSeat save(RouteSeat routeSeat);
}
