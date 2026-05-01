package vn.com.routex.hub.booking.service.infrastructure.cache.redis.models;

import lombok.Builder;
import vn.com.routex.hub.booking.service.domain.seat.SeatFloor;
import vn.com.routex.hub.booking.service.domain.seat.SeatStatus;


@Builder
public record RouteCacheSeat(
        String routeId,
        String seatId,
        String seatNo,
        String seatTemplateId,
        SeatStatus status,
        SeatFloor floor,
        int rowNo,
        int colNo
) {
}
