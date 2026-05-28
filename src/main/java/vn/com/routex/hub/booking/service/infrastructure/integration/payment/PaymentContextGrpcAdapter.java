package vn.com.routex.hub.booking.service.infrastructure.integration.payment;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.hub.booking.service.application.command.common.RequestContext;
import vn.com.routex.hub.booking.service.domain.paymentcontext.model.PaymentProcessingContext;
import vn.com.routex.hub.booking.service.domain.paymentcontext.port.PaymentContextQueryPort;
import vn.com.routex.hub.grpc.PaymentGrpcServiceGrpc;
import vn.com.routex.hub.grpc.PaymentRequestContext;
import vn.com.routex.hub.grpc.FetchPaymentContextRequest;
import vn.com.routex.hub.grpc.FetchPaymentContextResponse;

import java.time.OffsetDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PaymentContextGrpcAdapter implements PaymentContextQueryPort {

    @GrpcClient("paymentService")
    private PaymentGrpcServiceGrpc.PaymentGrpcServiceBlockingStub paymentServiceStub;
    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @Override
    public Optional<PaymentProcessingContext> findByBookingCode(String bookingCode, RequestContext context) {
        try {
            FetchPaymentContextResponse response = paymentServiceStub.fetchPaymentContext(
                    FetchPaymentContextRequest.newBuilder()
                            .setBookingCode(bookingCode)
                            .setContext(PaymentRequestContext.newBuilder()
                                    .setRequestId(context.requestId() != null ? context.requestId() : "")
                                    .setRequestDateTime(context.requestDateTime() != null ? context.requestDateTime() : "")
                                    .setChannel(context.channel() != null ? context.channel() : "")
                                    .build())
                            .build()
            );

            if (response == null || response.getPaymentId().isEmpty()) {
                return Optional.empty();
            }

            return Optional.of(PaymentProcessingContext.builder()
                    .paymentId(response.getPaymentId())
                    .bookingCode(response.getBookingCode())
                    .paymentStatus(response.getPaymentStatus())
                    .paidAt(response.getPaidAt().isEmpty() ? null : OffsetDateTime.parse(response.getPaidAt()))
                    .build());
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.NOT_FOUND) {
                sLog.info("[GRPC] Payment context not found for bookingCode: {}", bookingCode);
            } else {
                sLog.error("[GRPC] Error calling paymentService for bookingCode: {}", bookingCode, e);
            }
            return Optional.empty();
        } catch (Exception e) {
            sLog.error("[GRPC] Unexpected error calling paymentService for bookingCode: {}", bookingCode, e);
            return Optional.empty();
        }
    }
}
