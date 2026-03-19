package vn.com.routex.hub.booking.service.infrastructure.cache.redis.models;

import lombok.Builder;
import vn.com.routex.hub.booking.service.domain.seat.SeatStatus;


@Builder
public record RouteCacheSeat(
        String routeId,
        String seatNo,
        SeatStatus status
) {

}
