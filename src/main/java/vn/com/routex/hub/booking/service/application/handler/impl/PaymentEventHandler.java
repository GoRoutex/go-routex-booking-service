package vn.com.routex.hub.booking.service.application.handler.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.com.routex.hub.booking.service.application.handler.PaymentEvent;
import vn.com.routex.hub.booking.service.domain.booking.Booking;
import vn.com.routex.hub.booking.service.domain.booking.BookingRepository;
import vn.com.routex.hub.booking.service.domain.booking.BookingSeat;
import vn.com.routex.hub.booking.service.domain.booking.BookingSeatRepository;
import vn.com.routex.hub.booking.service.domain.booking.BookingSeatStatus;
import vn.com.routex.hub.booking.service.domain.booking.BookingStatus;
import vn.com.routex.hub.booking.service.domain.payment.Payment;
import vn.com.routex.hub.booking.service.domain.payment.PaymentRepository;
import vn.com.routex.hub.booking.service.domain.seat.RouteSeat;
import vn.com.routex.hub.booking.service.domain.seat.RouteSeatRepository;
import vn.com.routex.hub.booking.service.domain.seat.SeatStatus;
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

    private final BookingRepository bookingRepository;
    private final RouteSeatRepository routeSeatRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final PaymentRepository paymentRepository;

    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

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

        sLog.info("[BOOKING-SERVICE] Updated status for Payment Success Event, bookingId={}", aggregate.booking().getId());
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

        sLog.info("[BOOKING-SERVICE] Updated status for Payment Failed Event, bookingId={}", aggregate.booking().getId());
    }

    private BookingAggregate loadAggregate(
            String paymentId,
            String bookingId,
            String requestId,
            String requestDateTime,
            String channel
    ) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new BusinessException(
                        requestId, requestDateTime, channel,
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, "Payment not found")
                ));

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BusinessException(
                        requestId, requestDateTime, channel,
                ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, "Booking not found")
                ));

        BookingSeat bookingSeat = bookingSeatRepository.findByBookingId(booking.getId())
                .orElseThrow(() -> new BusinessException(
                        requestId, requestDateTime, channel,
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, "Booking Seat not found")
                ));

        RouteSeat routeSeat = routeSeatRepository
                .findByRouteIdAndSeatNo(bookingSeat.getRouteId(), bookingSeat.getSeatNo())
                .orElseThrow(() ->new BusinessException(
                        requestId, requestDateTime, channel,
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, "Route Seat not found")
                ));

        return new BookingAggregate(payment, booking, bookingSeat, routeSeat);
    }

    private void saveAggregate(BookingAggregate aggregate) {
        routeSeatRepository.save(aggregate.routeSeat());
        bookingSeatRepository.save(aggregate.bookingSeat());
        bookingRepository.save(aggregate.booking());
        paymentRepository.save(aggregate.payment());
    }
}
