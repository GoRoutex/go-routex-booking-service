package vn.com.routex.hub.booking.service.domain.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.com.routex.hub.booking.service.domain.booking.BookingStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    private String id;
    private String bookingCode;
    private String routeId;
    private String customerId;
    private Integer seatCount;
    private BigDecimal totalAmount;
    private String currency;
    private BookingStatus status;
    private OffsetDateTime heldAt;
    private OffsetDateTime holdUntil;
    private OffsetDateTime cancelledAt;
    private String note;
    private String creator;
}
