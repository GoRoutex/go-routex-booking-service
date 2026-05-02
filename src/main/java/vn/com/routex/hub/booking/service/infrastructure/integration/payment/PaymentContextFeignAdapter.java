package vn.com.routex.hub.booking.service.infrastructure.integration.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.application.command.common.RequestContext;
import vn.com.routex.hub.booking.service.domain.paymentcontext.model.PaymentProcessingContext;
import vn.com.routex.hub.booking.service.domain.paymentcontext.port.PaymentContextQueryPort;
import vn.com.routex.hub.booking.service.infrastructure.integration.payment.client.PaymentServiceContextFeignClient;
import vn.com.routex.hub.booking.service.infrastructure.integration.payment.dto.FetchPaymentContextClientRequest;
import vn.com.routex.hub.booking.service.infrastructure.integration.payment.dto.FetchPaymentContextClientResponse;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PaymentContextFeignAdapter implements PaymentContextQueryPort {

    private final PaymentServiceContextFeignClient paymentServiceContextFeignClient;

    @Override
    public Optional<PaymentProcessingContext> findByBookingCode(String bookingCode, RequestContext context) {
        FetchPaymentContextClientResponse response = paymentServiceContextFeignClient.fetchPaymentContext(
                FetchPaymentContextClientRequest.builder()
                        .requestId(context.requestId())
                        .requestDateTime(context.requestDateTime())
                        .channel(context.channel())
                        .data(FetchPaymentContextClientRequest.FetchPaymentContextClientRequestData.builder()
                                .bookingCode(bookingCode)
                                .build())
                        .build()
        );

        if (response == null || response.getData() == null) {
            return Optional.empty();
        }

        FetchPaymentContextClientResponse.FetchPaymentContextClientResponseData data = response.getData();
        return Optional.of(PaymentProcessingContext.builder()
                .paymentId(data.getPaymentId())
                .bookingCode(data.getBookingCode())
                .paymentStatus(data.getPaymentStatus())
                .paidAt(data.getPaidAt())
                .build());
    }
}
