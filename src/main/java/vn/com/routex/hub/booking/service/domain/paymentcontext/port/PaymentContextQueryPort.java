package vn.com.routex.hub.booking.service.domain.paymentcontext.port;

import vn.com.routex.hub.booking.service.application.command.common.RequestContext;
import vn.com.routex.hub.booking.service.domain.paymentcontext.model.PaymentProcessingContext;

import java.util.Optional;

public interface PaymentContextQueryPort {

    Optional<PaymentProcessingContext> findByBookingCode(String bookingCode, RequestContext context);
}
