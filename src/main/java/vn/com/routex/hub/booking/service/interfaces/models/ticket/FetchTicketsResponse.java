package vn.com.routex.hub.booking.service.interfaces.models.ticket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.booking.service.domain.ticket.TicketStatus;
import vn.com.routex.hub.booking.service.interfaces.models.base.BaseResponse;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class FetchTicketsResponse extends BaseResponse<FetchTicketsResponse.FetchTicketsResponsePage> {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class FetchTicketsResponsePage {
        private List<FetchTicketsResponseData> items;
        private Pagination pagination;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class FetchTicketsResponseData {
        private String id;
        private String ticketCode;
        private String bookingId;
        private String bookingSeatId;
        private String routeId;
        private String seatNumber;
        private String customerName;
        private String customerPhone;
        private BigDecimal price;
        private TicketStatus status;
        private OffsetDateTime issuedAt;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class Pagination {
        private int pageNumber;
        private int pageSize;
        private long totalElements;
        private int totalPages;
    }
}
