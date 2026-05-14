package vn.com.routex.hub.booking.service.domain.payment.port;


import vn.com.routex.hub.booking.service.domain.booking.PaymentStatus;
import vn.com.routex.hub.booking.service.domain.payment.PaymentMethod;
import vn.com.routex.hub.booking.service.domain.payment.model.PaymentAggregate;

import java.util.Optional;

public interface PaymentRepositoryPort {
    Optional<PaymentAggregate> findById(String paymentId);

    Optional<PaymentAggregate> findByBookingCodeAndMethodAndStatus(String bookingCode, PaymentMethod method, PaymentStatus status);

    PaymentAggregate save(PaymentAggregate paymentAggregate);

    Optional<PaymentAggregate> findByTxnRef(String txnRef);

    Optional<PaymentAggregate> findByBookingCode(String bookingCode);
}
