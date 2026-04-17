package vn.com.routex.hub.booking.service.domain.seat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.booking.service.domain.seat.SeatStatus;

import java.time.OffsetDateTime;

/**
 * Domain model for route seat.
 * Persistence concerns (JPA annotations, table/column mapping) live in infrastructure layer:
 * {@code infrastructure.persistence.jpa.route.entity.RouteSeatEntity}.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class RouteSeat {

    private Integer id;
    private String routeId;
    private String seatNo;
    private SeatStatus status;
    private String ticketId;
    private OffsetDateTime holdUntil;
    private String holdBy;
    private String creator;
}

