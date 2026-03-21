package vn.com.routex.hub.booking.service.application.dto.seat;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Builder
public record HoldSeatResult(
        List<HoldSeatItemResult> seats
) {
    @Builder
    public record HoldSeatBookingResult(
            String bookingId,
            String bookingCode,
            OffsetDateTime holdUntil,
            Integer seatCount,
            BigDecimal totalAmount,
            String currency
    ) {
    }

    @Builder
    public record HoldSeatItemResult(
            String routeId,
            String seatNo,
            String status,
            String holdToken,
            HoldSeatBookingResult booking
    ) {
    }
}
