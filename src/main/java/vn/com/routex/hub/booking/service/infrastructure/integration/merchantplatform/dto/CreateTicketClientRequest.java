package vn.com.routex.hub.booking.service.infrastructure.integration.merchantplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.booking.service.interfaces.models.base.BaseRequest;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CreateTicketClientRequest extends BaseRequest {
    private List<CreateTicketClientData> data;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class CreateTicketClientData {
        private String bookingId;
        private String bookingSeatId;
        private String merchantId;
        private String tripId;
        private String vehicleId;
        private String seatNumber;
        private String customerName;
        private String customerPhone;
        private String customerEmail;
        private BigDecimal price;
        private OffsetDateTime issuedAt;
        private String creator;
        private String pickupType;
        private String pickupStopId;
        private String pickupAddress;
        private String dropoffType;
        private String dropoffStopId;
        private String dropoffAddress;
    }
}
