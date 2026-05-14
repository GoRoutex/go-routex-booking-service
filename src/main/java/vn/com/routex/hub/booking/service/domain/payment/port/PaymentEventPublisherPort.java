package vn.com.routex.hub.booking.service.domain.payment.port;


import vn.com.routex.hub.booking.service.application.command.common.RequestContext;
import vn.com.routex.hub.booking.service.domain.payment.model.PaymentAggregate;

public interface PaymentEventPublisherPort {
    void publishPaymentSucceeded(RequestContext context, PaymentAggregate paymentAggregate);

    void publishPaymentFailed(RequestContext context, PaymentAggregate paymentAggregate, String reason);
}
