package vn.com.routex.hub.booking.service.application.handler.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.com.routex.hub.booking.service.application.handler.PaymentEvent;
import vn.com.routex.hub.booking.service.controller.models.base.BaseRequest;
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
import vn.com.routex.hub.booking.service.domain.ticket.TicketStatus;
import vn.com.routex.hub.booking.service.domain.ticket.model.Ticket;
import vn.com.routex.hub.booking.service.domain.ticket.port.TicketRepositoryPort;
import vn.com.routex.hub.booking.service.infrastructure.kafka.config.KafkaEventPublisher;
import vn.com.routex.hub.booking.service.infrastructure.kafka.event.DomainEvent;
import vn.com.routex.hub.booking.service.infrastructure.kafka.event.PaymentFailedEvent;
import vn.com.routex.hub.booking.service.infrastructure.kafka.event.PaymentSuccessEvent;
import vn.com.routex.hub.booking.service.infrastructure.kafka.event.TicketIssuedEvent;
import vn.com.routex.hub.booking.service.infrastructure.kafka.record.BookingAggregate;
import vn.com.routex.hub.booking.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.booking.service.infrastructure.persistence.log.SystemLog;
import vn.com.routex.hub.booking.service.infrastructure.persistence.utils.ExceptionUtils;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;

@RequiredArgsConstructor
@Component
public class PaymentEventHandler implements PaymentEvent {

    private final BookingRepositoryPort bookingRepositoryPort;
    private final RouteSeatRepositoryPort routeSeatRepositoryPort;
    private final BookingSeatRepositoryPort bookingSeatRepositoryPort;
    private final PaymentRepositoryPort paymentRepositoryPort;
    private final TicketRepositoryPort ticketRepositoryPort;
    private final KafkaEventPublisher kafkaEventPublisher;

    @Value("${spring.kafka.topics.booking}")
    private String bookingTopic;

    @Value("${spring.kafka.events.ticket-issued}")
    private String ticketIssuedEvent;

    private final SystemLog sLog = SystemLog.getLogger(this.getClass());


    /**
     * Update status for Booking, BookingSeat, RouteSeat after success payment
     * @param event
     */
    @Override
    @Transactional
    public void updateSuccessPayment(DomainEvent event, BaseRequest context, PaymentSuccessEvent payload) {
        BookingAggregate aggregate = loadAggregate(
                payload.paymentId(),
                payload.bookingId(),
                context.getRequestId(),
                context.getRequestDateTime(),
                context.getChannel()
        );

        if (aggregate.booking().getStatus() == BookingStatus.CONFIRMED) {
            sLog.info("[BOOKING-SERVICE] Payment success event already processed for bookingId={}", aggregate.booking().getId());
            return;
        }

        aggregate.routeSeats().forEach(routeSeat -> routeSeat.setStatus(SeatStatus.SOLD));
        List<Ticket> issuedTickets = createTickets(aggregate);
        List<BookingSeat> reservedSeats = attachIssuedTickets(aggregate.bookingSeats(), issuedTickets);
        aggregate.booking().setStatus(BookingStatus.CONFIRMED);
        saveAggregate(aggregate, reservedSeats, issuedTickets);
        publishTicketIssuedEvent(context, aggregate, issuedTickets);
    }

    @Override
    @Transactional
    public void updateFailEvent(DomainEvent event, BaseRequest context, PaymentFailedEvent payload) {
        BookingAggregate aggregate = loadAggregate(
                payload.paymentId(),
                payload.bookingId(),
                context.getRequestId(),
                context.getRequestDateTime(),
                context.getChannel()
        );

        if (aggregate.booking().getStatus() == BookingStatus.CANCELLED) {
            sLog.info("[BOOKING-SERVICE] Payment failed event already processed for bookingId={}", aggregate.booking().getId());
            return;
        }

        aggregate.routeSeats().forEach(routeSeat -> routeSeat.setStatus(SeatStatus.AVAILABLE));
        List<BookingSeat> cancelledSeats = aggregate.bookingSeats().stream()
                .map(this::toCancelledBookingSeat)
                .toList();
        aggregate.booking().setStatus(BookingStatus.CANCELLED);

        saveAggregate(aggregate, cancelledSeats, List.of());
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

        List<BookingSeat> bookingSeats = bookingSeatRepositoryPort.findAllByBookingId(booking.getId());
        if (bookingSeats.isEmpty()) {
            throw new BusinessException(
                        requestId, requestDateTime, channel,
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, "Booking Seat not found")
                );
        }

        List<RouteSeat> routeSeats = bookingSeats.stream()
                .map(bookingSeat -> routeSeatRepositoryPort.findByRouteIdAndSeatNo(bookingSeat.getRouteId(), bookingSeat.getSeatNo())
                        .orElseThrow(() -> new BusinessException(
                                requestId, requestDateTime, channel,
                                ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, "Route Seat not found")
                        )))
                .toList();

        return new BookingAggregate(payment, booking, bookingSeats, routeSeats);
    }

    private List<Ticket> createTickets(BookingAggregate aggregate) {
        OffsetDateTime issuedAt = aggregate.payment().getPaidAt() != null
                ? aggregate.payment().getPaidAt()
                : OffsetDateTime.now();

        return aggregate.bookingSeats().stream()
                .map(bookingSeat -> (Ticket) Ticket.builder()
                        .id(UUID.randomUUID().toString())
                        .ticketCode(ticketRepositoryPort.generateTicketCode())
                        .bookingId(aggregate.booking().getId())
                        .bookingSeatId(bookingSeat.getId())
                        .routeId(bookingSeat.getRouteId())
                        .seatNumber(bookingSeat.getSeatNo())
                        .customerName(aggregate.booking().getCustomerName())
                        .customerPhone(aggregate.booking().getCustomerPhone())
                        .price(bookingSeat.getPrice())
                        .status(TicketStatus.ISSUED)
                        .issuedAt(issuedAt)
                        .createdAt(issuedAt)
                        .createdBy(aggregate.booking().getCreator())
                        .updatedAt(issuedAt)
                        .updatedBy(aggregate.booking().getCreator())
                        .build())
                .toList();
    }

    private List<BookingSeat> attachIssuedTickets(List<BookingSeat> bookingSeats, List<Ticket> tickets) {
        return bookingSeats.stream()
                .map(bookingSeat -> {
                    Ticket matchedTicket = tickets.stream()
                            .filter(ticket -> ticket.getBookingSeatId().equals(bookingSeat.getId()))
                            .findFirst()
                            .orElseThrow();

                    return BookingSeat.builder()
                            .id(bookingSeat.getId())
                            .bookingId(bookingSeat.getBookingId())
                            .routeId(bookingSeat.getRouteId())
                            .seatNo(bookingSeat.getSeatNo())
                            .price(bookingSeat.getPrice())
                            .status(BookingSeatStatus.RESERVED)
                            .ticketId(matchedTicket.getId())
                            .creator(bookingSeat.getCreator())
                            .build();
                })
                .toList();
    }

    private BookingSeat toCancelledBookingSeat(BookingSeat bookingSeat) {
        return BookingSeat.builder()
                .id(bookingSeat.getId())
                .bookingId(bookingSeat.getBookingId())
                .routeId(bookingSeat.getRouteId())
                .seatNo(bookingSeat.getSeatNo())
                .price(bookingSeat.getPrice())
                .status(BookingSeatStatus.CANCELLED)
                .ticketId(bookingSeat.getTicketId())
                .creator(bookingSeat.getCreator())
                .build();
    }

    private void saveAggregate(BookingAggregate aggregate, List<BookingSeat> bookingSeats, List<Ticket> tickets) {
        routeSeatRepositoryPort.saveAll(aggregate.routeSeats());
        if (!tickets.isEmpty()) {
            ticketRepositoryPort.saveAll(tickets);
        }
        bookingSeatRepositoryPort.saveAll(bookingSeats);
        bookingRepositoryPort.save(aggregate.booking());
        paymentRepositoryPort.save(aggregate.payment());
    }

    private void publishTicketIssuedEvent(BaseRequest context,
                                          BookingAggregate aggregate,
                                          List<Ticket> issuedTickets) {
        TicketIssuedEvent payload = TicketIssuedEvent.builder()
                .bookingId(aggregate.booking().getId())
                .bookingCode(aggregate.booking().getBookingCode())
                .customerId(aggregate.booking().getCustomerId())
                .customerName(aggregate.booking().getCustomerName())
                .customerPhone(aggregate.booking().getCustomerPhone())
                .customerEmail(aggregate.booking().getCustomerEmail())
                .routeId(aggregate.booking().getRouteId())
                .totalAmount(aggregate.booking().getTotalAmount())
                .currency(aggregate.booking().getCurrency())
                .paidAt(aggregate.payment().getPaidAt())
                .tickets(issuedTickets.stream()
                        .map(ticket -> TicketIssuedEvent.TicketIssuedItem.builder()
                                .ticketId(ticket.getId())
                                .ticketCode(ticket.getTicketCode())
                                .bookingSeatId(ticket.getBookingSeatId())
                                .seatNumber(ticket.getSeatNumber())
                                .price(ticket.getPrice())
                                .status(ticket.getStatus())
                                .issuedAt(ticket.getIssuedAt())
                                .build())
                        .toList())
                .build();

        kafkaEventPublisher.publish(
                context.getRequestId(),
                context.getRequestDateTime(),
                context.getChannel(),
                bookingTopic,
                ticketIssuedEvent,
                aggregate.booking().getId(),
                payload
        );
    }
}
