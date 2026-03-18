package vn.com.routex.hub.booking.service.domain.seat;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.routex.hub.booking.service.controller.models.seat.RouteSeatView;

import java.util.List;
import java.util.Optional;

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

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = """
                   select rs
                   from RouteSeat rs
                   where rs.routeId = :routeId
                   and rs.seatNo in :seatNos
                   """)
    List<RouteSeat> findAllByRouteIdAndSeatNoInForUpdate(@Param("routeId") String routeId,
                                                         @Param("seatNos") List<String> seatNos);

    Optional<RouteSeat> findByRouteId(String routeId);

    Optional<RouteSeat> findByRouteIdAndSeatNo(String routeId, String seatNo);
}
