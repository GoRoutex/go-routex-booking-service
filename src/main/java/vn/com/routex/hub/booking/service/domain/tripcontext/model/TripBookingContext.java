package vn.com.routex.hub.booking.service.domain.tripcontext.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripBookingContext {
    private String tripId;
    private String routeId;
    private String merchantId;
    private String vehicleId;
    private BigDecimal ticketPrice;
    private String pickupBranch;
    private String originName;
    private String destinationName;
    private String routeStatus;
    private String tripStatus;
}
