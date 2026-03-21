package vn.com.routex.hub.booking.service.application.dto.seat;

import lombok.Builder;

import java.util.List;

@Builder
public record GetAllSeatResult(
        List<SeatItemResult> seats
) {
}
