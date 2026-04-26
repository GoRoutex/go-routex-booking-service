package vn.com.routex.hub.booking.service.domain.seat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.booking.service.domain.auditing.AbstractAuditingEntity;
import vn.com.routex.hub.booking.service.domain.seat.SeatStatus;

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
public class RouteSeat extends AbstractAuditingEntity {
    private String id;
    private String routeId;
    private String seatNo;
    private SeatStatus status;
    private String creator;
}

