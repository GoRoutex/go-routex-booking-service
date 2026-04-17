package vn.com.routex.hub.booking.service.domain.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.com.routex.hub.booking.service.domain.booking.BookingSeatStatus;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingSeat {
    private String id;
    private String bookingId;
    private String routeId;
    private String seatNo;
    private BigDecimal price;
    private BookingSeatStatus status;
    private String ticketId;
    private String creator;
}
