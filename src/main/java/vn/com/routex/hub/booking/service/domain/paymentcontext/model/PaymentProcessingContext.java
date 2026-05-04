package vn.com.routex.hub.booking.service.domain.paymentcontext.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentProcessingContext {
    private String paymentId;
    private String bookingCode;
    private String paymentStatus;
    private OffsetDateTime paidAt;
}
