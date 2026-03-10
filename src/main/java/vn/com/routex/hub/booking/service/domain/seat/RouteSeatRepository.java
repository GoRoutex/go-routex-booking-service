package vn.com.routex.hub.booking.service.domain.seat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.routex.hub.booking.service.interfaces.models.seat.RouteSeatView;

import java.util.List;

public interface RouteSeatRepository extends JpaRepository<RouteSeat, String> {

    boolean existsByRouteId(String routeId);


    @Query(value="""
            SELECT rs.ROUTE_ID AS routeId, count(rs) AS availableSeat
                        FROM ROUTE_SEAT RS
                        WHERE RS.route_id in :routeIds
                        AND status = :status
                        GROUP BY rs.ROUTE_ID;
            """,
            nativeQuery = true)
    List<RouteSeatView> countByRouteIdAndStatus(@Param("routeIds") List<String> routeIds,
                                                @Param("status") String status);

    List<String> routeId(String routeId);

    List<RouteSeat> findAllByRouteIdOrderBySeatNoAsc(String routeId);

    List<RouteSeat> findAllByRouteIdAndSeatNoIn(String routeId, List<String> seatNos);
}
