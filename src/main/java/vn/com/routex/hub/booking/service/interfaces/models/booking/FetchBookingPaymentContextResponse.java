package vn.com.routex.hub.booking.service.interfaces.models.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.booking.service.domain.booking.BookingStatus;
import vn.com.routex.hub.booking.service.interfaces.models.base.BaseResponse;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@SuperBuilder
public class FetchBookingPaymentContextResponse extends BaseResponse<FetchBookingPaymentContextResponse.Data> {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class Data {
        private String bookingId;
        private String bookingCode;
        private String customerId;
        private BigDecimal totalAmount;
        private String currency;
        private BookingStatus bookingStatus;
        private OffsetDateTime holdUntil;
    }
}
