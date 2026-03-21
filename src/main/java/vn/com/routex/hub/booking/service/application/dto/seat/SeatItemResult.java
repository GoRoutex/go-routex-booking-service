package vn.com.routex.hub.booking.service.application.dto.seat;

import lombok.Builder;
import vn.com.routex.hub.booking.service.domain.seat.SeatStatus;

@Builder
public record SeatItemResult(
        String routeId,
        String seatNo,
        SeatStatus status
) {
}
