package vn.com.routex.hub.booking.service.application.handler.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.com.routex.hub.booking.service.application.handler.PaymentEvent;
import vn.com.routex.hub.booking.service.domain.booking.BookingSeatStatus;
import vn.com.routex.hub.booking.service.domain.booking.BookingStatus;
import vn.com.routex.hub.booking.service.domain.booking.model.Booking;
import vn.com.routex.hub.booking.service.domain.booking.model.BookingSeat;
import vn.com.routex.hub.booking.service.domain.booking.port.BookingRepositoryPort;
import vn.com.routex.hub.booking.service.domain.booking.port.BookingSeatRepositoryPort;
import vn.com.routex.hub.booking.service.domain.payment.model.Payment;
import vn.com.routex.hub.booking.service.domain.payment.port.PaymentRepositoryPort;
import vn.com.routex.hub.booking.service.domain.seat.SeatStatus;
import vn.com.routex.hub.booking.service.domain.seat.model.RouteSeat;
import vn.com.routex.hub.booking.service.domain.seat.port.RouteSeatRepositoryPort;
import vn.com.routex.hub.booking.service.domain.ticket.port.TicketRepositoryPort;
import vn.com.routex.hub.booking.service.infrastructure.kafka.event.PaymentFailedEvent;
import vn.com.routex.hub.booking.service.infrastructure.kafka.event.PaymentSuccessEvent;
import vn.com.routex.hub.booking.service.infrastructure.kafka.model.KafkaEventMessage;
import vn.com.routex.hub.booking.service.infrastructure.kafka.record.BookingAggregate;
import vn.com.routex.hub.booking.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.booking.service.infrastructure.persistence.log.SystemLog;
import vn.com.routex.hub.booking.service.infrastructure.persistence.utils.ExceptionUtils;

import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;

@RequiredArgsConstructor
@Component
public class PaymentEventHandler implements PaymentEvent {

    private final BookingRepositoryPort bookingRepositoryPort;
    private final RouteSeatRepositoryPort routeSeatRepositoryPort;
    private final BookingSeatRepositoryPort bookingSeatRepositoryPort;
    private final PaymentRepositoryPort paymentRepositoryPort;
    private final TicketRepositoryPort ticketRepositoryPort;

    private final SystemLog sLog = SystemLog.getLogger(this.getClass());


    /**
     * Update status for Booking, BookingSeat, RouteSeat after success payment
     * @param event
     */
    @Override
    @Transactional
    public void updateSuccessPayment(KafkaEventMessage<PaymentSuccessEvent> event) {
        BookingAggregate aggregate = loadAggregate(
                event.data().paymentId(),
                event.data().bookingId(),
                event.requestId(),
                event.requestDateTime(),
                event.channel()
        );

        if (aggregate.booking().getStatus() == BookingStatus.CONFIRMED) {
            sLog.info("[BOOKING-SERVICE] Payment success event already processed for bookingId={}", aggregate.booking().getId());
            return;
        }

        aggregate.routeSeat().setStatus(SeatStatus.SOLD);
        aggregate.bookingSeat().setStatus(BookingSeatStatus.RESERVED);
        aggregate.booking().setStatus(BookingStatus.CONFIRMED);
        saveAggregate(aggregate);
        // TODO: Create ticket after payment success (domain port currently only exposes code generation).
    }

    @Override
    @Transactional
    public void updateFailEvent(KafkaEventMessage<PaymentFailedEvent> event) {
        BookingAggregate aggregate = loadAggregate(
                event.data().paymentId(),
                event.data().bookingId(),
                event.requestId(),
                event.requestDateTime(),
                event.channel()
        );

        if (aggregate.booking().getStatus() == BookingStatus.CANCELLED) {
            sLog.info("[BOOKING-SERVICE] Payment failed event already processed for bookingId={}", aggregate.booking().getId());
            return;
        }

        aggregate.routeSeat().setStatus(SeatStatus.AVAILABLE);
        aggregate.bookingSeat().setStatus(BookingSeatStatus.CANCELLED);
        aggregate.booking().setStatus(BookingStatus.CANCELLED);

        saveAggregate(aggregate);
    }

    private BookingAggregate loadAggregate(
            String paymentId,
            String bookingId,
            String requestId,
        String requestDateTime,
        String channel
    ) {
        Payment payment = paymentRepositoryPort.findById(paymentId)
                .orElseThrow(() -> new BusinessException(
                        requestId, requestDateTime, channel,
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, "Payment not found")
                ));

        Booking booking = bookingRepositoryPort.findById(bookingId)
                .orElseThrow(() -> new BusinessException(
                        requestId, requestDateTime, channel,
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, "Booking not found")
                ));

        BookingSeat bookingSeat = bookingSeatRepositoryPort.findByBookingId(booking.getId())
                .orElseThrow(() -> new BusinessException(
                        requestId, requestDateTime, channel,
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, "Booking Seat not found")
                ));

        RouteSeat routeSeat = routeSeatRepositoryPort.findByRouteIdAndSeatNo(bookingSeat.getRouteId(), bookingSeat.getSeatNo())
                .orElseThrow(() -> new BusinessException(
                        requestId, requestDateTime, channel,
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, "Route Seat not found")
                ));

        return new BookingAggregate(payment, booking, bookingSeat, routeSeat);
    }

    private void saveAggregate(BookingAggregate aggregate) {
        routeSeatRepositoryPort.save(aggregate.routeSeat());
        bookingSeatRepositoryPort.save(aggregate.bookingSeat());
        bookingRepositoryPort.save(aggregate.booking());
        paymentRepositoryPort.save(aggregate.payment());
    }
}
