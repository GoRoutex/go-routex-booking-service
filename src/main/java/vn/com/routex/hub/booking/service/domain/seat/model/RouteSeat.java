package vn.com.routex.hub.booking.service.domain.seat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.com.routex.hub.booking.service.domain.seat.SeatStatus;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteSeat {
    private Integer id;
    private String routeId;
    private String seatNo;
    private SeatStatus status;
    private String creator;
}
