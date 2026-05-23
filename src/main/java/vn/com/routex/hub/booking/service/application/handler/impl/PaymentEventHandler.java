package vn.com.routex.hub.booking.service.application.handler.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.hub.booking.service.application.handler.PaymentEvent;
import vn.com.routex.hub.booking.service.application.services.OutBoxService;
import vn.com.routex.hub.booking.service.domain.booking.BookingSeatStatus;
import vn.com.routex.hub.booking.service.domain.booking.BookingStatus;
import vn.com.routex.hub.booking.service.domain.booking.PaymentStatus;
import vn.com.routex.hub.booking.service.domain.booking.model.Booking;
import vn.com.routex.hub.booking.service.domain.booking.model.BookingSeat;
import vn.com.routex.hub.booking.service.domain.booking.port.BookingRepositoryPort;
import vn.com.routex.hub.booking.service.domain.booking.port.BookingSeatRepositoryPort;
import vn.com.routex.hub.booking.service.domain.payment.model.PaymentAggregate;
import vn.com.routex.hub.booking.service.domain.payment.port.PaymentRepositoryPort;
import vn.com.routex.hub.booking.service.domain.seat.SeatStatus;
import vn.com.routex.hub.booking.service.domain.seat.model.TripSeat;
import vn.com.routex.hub.booking.service.domain.seat.port.TripSeatRepositoryPort;
import vn.com.routex.hub.booking.service.domain.ticket.TicketStatus;
import vn.com.routex.hub.booking.service.domain.ticket.model.Ticket;
import vn.com.routex.hub.booking.service.infrastructure.cache.mapper.TripCacheMapper;
import vn.com.routex.hub.booking.service.infrastructure.cache.redis.models.TripCacheSeat;
import vn.com.routex.hub.booking.service.infrastructure.cache.redis.service.TripSeatCacheService;
import vn.com.routex.hub.booking.service.infrastructure.integration.merchantplatform.client.MerchantTicketFeignClient;
import vn.com.routex.hub.booking.service.infrastructure.integration.merchantplatform.dto.CreateTicketClientRequest;
import vn.com.routex.hub.booking.service.infrastructure.integration.merchantplatform.dto.CreateTicketClientResponse;
import vn.com.routex.hub.booking.service.infrastructure.kafka.event.DomainEvent;
import vn.com.routex.hub.booking.service.infrastructure.kafka.event.PaymentFailedEvent;
import vn.com.routex.hub.booking.service.infrastructure.kafka.event.PaymentSuccessEvent;
import vn.com.routex.hub.booking.service.infrastructure.kafka.event.TicketIssuedEvent;
import vn.com.routex.hub.booking.service.infrastructure.kafka.record.BookingAggregate;
import vn.com.routex.hub.booking.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.booking.service.infrastructure.persistence.utils.DateTimeUtils;
import vn.com.routex.hub.booking.service.infrastructure.persistence.utils.ExceptionUtils;
import vn.com.routex.hub.booking.service.interfaces.models.base.BaseRequest;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.PAYMENT_NOT_FOUND;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;

@RequiredArgsConstructor
@Component
public class PaymentEventHandler implements PaymentEvent {

    private final BookingRepositoryPort bookingRepositoryPort;
    private final TripSeatRepositoryPort tripSeatRepositoryPort;
    private final BookingSeatRepositoryPort bookingSeatRepositoryPort;
    private final MerchantTicketFeignClient merchantTicketFeignClient;
    private final PaymentRepositoryPort paymentRepositoryPort;
    private final TripSeatCacheService tripSeatCacheService;
    private final OutBoxService outBoxService;
    private final TripCacheMapper tripCacheMapper;

    @Value("${spring.kafka.topics.booking}")
    private String bookingTopic;

    @Value("${spring.kafka.events.ticket-issued}")
    private String ticketIssuedEvent;

    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @Override
    @Transactional
    public void updateSuccessPayment(DomainEvent event, BaseRequest context, PaymentSuccessEvent payload) {
        sLog.info("Updating success payment: {}", payload);
        BookingAggregate aggregate = loadAggregate(
                payload.bookingCode(),
                context.getRequestId(),
                context.getRequestDateTime(),
                context.getChannel()
        );

        // 1. Guard Clauses: Kiểm tra điều kiện dừng sớm
        if (isAlreadyProcessed(aggregate)) return;
        if (isInvalidStatus(aggregate)) return;

        // 2. Business Logic: Thực hiện nghiệp vụ chính
        OffsetDateTime paidAt = OffsetDateTime.now();
        List<Ticket> issuedTickets = processSuccessfulBooking(aggregate, context, paidAt);

        // 3. Persistence: Lưu trữ dữ liệu
        saveAggregate(aggregate, aggregate.bookingSeats(), aggregate.paymentAggregate(), aggregate.tripSeats());

        // 4. Cache & Integration: Cập nhật hạ tầng liên quan
        updateTripSeatCache(aggregate);
        publishTicketIssuedEvent(context, aggregate, issuedTickets, paidAt);
    }

    private boolean isAlreadyProcessed(BookingAggregate aggregate) {
        if (aggregate.booking().getStatus() == BookingStatus.CONFIRMED) {
            sLog.info("[BOOKING-SERVICE] Payment success already processed for bookingId={}", aggregate.booking().getId());
            return true;
        }
        return false;
    }

    private boolean isInvalidStatus(BookingAggregate aggregate) {
        BookingStatus status = aggregate.booking().getStatus();
        if (status == BookingStatus.CANCELLED || status == BookingStatus.EXPIRED) {
            sLog.info("[BOOKING-SERVICE] Ignore payment for bookingId={} because status={}", aggregate.booking().getId(), status);
            return true;
        }
        return false;
    }

    private List<Ticket> processSuccessfulBooking(BookingAggregate aggregate, BaseRequest context, OffsetDateTime paidAt) {
        // Cập nhật trạng thái ghế trong Trip
        aggregate.tripSeats().forEach(seat -> seat.setStatus(SeatStatus.SOLD));
        // Xuất vé và gắn vào booking seats
        List<Ticket> tickets = createTickets(aggregate, paidAt);
        attachIssuedTickets(aggregate.bookingSeats(), tickets);

        // Cập nhật trạng thái thanh toán và booking
        aggregate.paymentAggregate().markPaid(paidAt);
        aggregate.booking().setStatus(BookingStatus.CONFIRMED);

        return tickets;
    }

    private void updateTripSeatCache(BookingAggregate aggregate) {
        List<TripCacheSeat> cacheSeats = aggregate.tripSeats().stream()
                .map(tripCacheMapper::toCacheModel) // Tách logic mapping ra Mapper class
                .sorted(Comparator.comparing(TripCacheSeat::seatNo))
                .toList();

        tripSeatCacheService.updateSeatsStatus(aggregate.booking().getTripId(), cacheSeats);
    }

    @Override
    @Transactional
    public void updateFailEvent(DomainEvent event, BaseRequest context, PaymentFailedEvent payload) {
        BookingAggregate aggregate = loadAggregate(
                payload.bookingCode(),
                context.getRequestId(),
                context.getRequestDateTime(),
                context.getChannel()
        );

        if (aggregate.booking().getStatus() == BookingStatus.CANCELLED
                || aggregate.booking().getStatus() == BookingStatus.EXPIRED) {
            sLog.info("[BOOKING-SERVICE] Payment failed event ignored for bookingId={} because current status={}",
                    aggregate.booking().getId(), aggregate.booking().getStatus());
            return;
        }

        aggregate.tripSeats().forEach(routeSeat -> routeSeat.setStatus(SeatStatus.AVAILABLE));
        List<BookingSeat> cancelledSeats = aggregate.bookingSeats().stream()
                .map(this::toCancelledBookingSeat)
                .toList();
        aggregate.booking().setStatus(BookingStatus.CANCELLED);
        PaymentAggregate paymentAggregate = aggregate.paymentAggregate();
        paymentAggregate.setStatus(PaymentStatus.FAILED);
        paymentAggregate.setFailedAt(OffsetDateTime.now());
        paymentAggregate.setFailureReason(payload.reason());
        saveAggregate(aggregate, cancelledSeats, paymentAggregate, aggregate.tripSeats());
    }

    private BookingAggregate loadAggregate(
            String bookingCode,
            String requestId,
            String requestDateTime,
            String channel
    ) {
        Booking booking = bookingRepositoryPort.findByBookingCodeForUpdate(bookingCode)
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

        PaymentAggregate paymentAggregate = paymentRepositoryPort.findByBookingCode(bookingCode)
                .orElseThrow(() -> new BusinessException(requestId, requestDateTime, channel,
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(PAYMENT_NOT_FOUND, bookingCode))));

        List<TripSeat> tripSeats = bookingSeats.stream()
                .map(bookingSeat -> tripSeatRepositoryPort.findByTripIdAndSeatNo(bookingSeat.getTripId(), bookingSeat.getSeatNo())
                        .orElseThrow(() -> new BusinessException(
                                requestId, requestDateTime, channel,
                                ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, "Route Seat not found")
                        )))
                .toList();

        return new BookingAggregate(booking, bookingSeats, tripSeats, paymentAggregate);
    }

    private List<Ticket> createTickets(BookingAggregate aggregate, OffsetDateTime paidAt) {
        OffsetDateTime issuedAt = paidAt != null ? paidAt : OffsetDateTime.now();
        CreateTicketClientRequest request = CreateTicketClientRequest.builder()
                .requestId(UUID.randomUUID().toString())
                .requestDateTime(DateTimeUtils.getCurrentRequestDateTime())
                .channel("INTERNAL")
                .data(aggregate.bookingSeats().stream()
                        .map(bookingSeat -> CreateTicketClientRequest.CreateTicketClientData.builder()
                                .bookingId(aggregate.booking().getId())
                                .bookingSeatId(bookingSeat.getId())
                                .merchantId(aggregate.booking().getMerchantId())
                                .tripId(bookingSeat.getTripId())
                                .vehicleId(aggregate.booking().getVehicleId())
                                .seatNumber(bookingSeat.getSeatNo())
                                .customerName(aggregate.booking().getCustomerName())
                                .customerPhone(aggregate.booking().getCustomerPhone())
                                .customerEmail(aggregate.booking().getCustomerEmail())
                                .price(bookingSeat.getPrice())
                                .issuedAt(issuedAt)
                                .creator(aggregate.booking().getCreator())
                                .pickupType(aggregate.booking().getPickupType())
                                .pickupStopId(aggregate.booking().getPickupStopId())
                                .pickupAddress(aggregate.booking().getPickupAddress())
                                .dropoffType(aggregate.booking().getDropoffType())
                                .dropoffStopId(aggregate.booking().getDropoffStopId())
                                .dropoffAddress(aggregate.booking().getDropoffAddress())
                                .build())
                        .collect(Collectors.toList()))
                .build();


        sLog.info("Ticket request: {}", request);

        CreateTicketClientResponse response = merchantTicketFeignClient.createTickets(request);

        sLog.info("Ticket Response: {}", response);
        return response.getData().stream()
                .map(item -> Ticket.builder()
                        .id(item.getTicketId())
                        .ticketCode(item.getTicketCode())
                        .bookingId(aggregate.booking().getId())
                        .bookingSeatId(item.getBookingSeatId())
                        .vehicleId(aggregate.booking().getVehicleId())
                        .tripId(aggregate.booking().getTripId())
                        .seatNumber(aggregate.bookingSeats().stream()
                                .filter(s -> s.getId().equals(item.getBookingSeatId()))
                                .findFirst()
                                .map(BookingSeat::getSeatNo)
                                .orElse(""))
                        .price(aggregate.bookingSeats().stream()
                                .filter(s -> s.getId().equals(item.getBookingSeatId()))
                                .findFirst()
                                .map(BookingSeat::getPrice)
                                .orElse(java.math.BigDecimal.ZERO))
                        .status(TicketStatus.valueOf(item.getStatus()))
                        .issuedAt(issuedAt)
                        .pickupType(aggregate.booking().getPickupType())
                        .pickupStopId(aggregate.booking().getPickupStopId())
                        .pickupAddress(aggregate.booking().getPickupAddress())
                        .dropoffType(aggregate.booking().getDropoffType())
                        .dropoffStopId(aggregate.booking().getDropoffStopId())
                        .dropoffAddress(aggregate.booking().getDropoffAddress())
                        .build())
                .collect(Collectors.toList());
    }


    private void attachIssuedTickets(List<BookingSeat> bookingSeats, List<Ticket> tickets) {

        bookingSeats.stream()
                .map(bookingSeat -> {
                    sLog.info("Ticket: {}", tickets);
                    Ticket matchedTicket = tickets.stream()
                            .filter(ticket -> ticket.getBookingSeatId().equals(bookingSeat.getId()))
                            .findFirst()
                            .orElseThrow();

                    return BookingSeat.builder()
                            .id(bookingSeat.getId())
                            .bookingId(bookingSeat.getBookingId())
                            .tripId(bookingSeat.getTripId())
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
                .tripId(bookingSeat.getTripId())
                .seatNo(bookingSeat.getSeatNo())
                .price(bookingSeat.getPrice())
                .status(BookingSeatStatus.CANCELLED)
                .ticketId(bookingSeat.getTicketId())
                .creator(bookingSeat.getCreator())
                .build();
    }

    private void saveAggregate(BookingAggregate aggregate, List<BookingSeat> bookingSeats, PaymentAggregate paymentAggregate, List<TripSeat> tripSeats) {
        bookingSeatRepositoryPort.saveAll(bookingSeats);
        bookingRepositoryPort.save(aggregate.booking());
        paymentRepositoryPort.save(paymentAggregate);
        tripSeatRepositoryPort.saveAll(tripSeats);
    }

    private void publishTicketIssuedEvent(BaseRequest context,
                                          BookingAggregate aggregate,
                                          List<Ticket> issuedTickets,
                                          OffsetDateTime paidAt) {
        TicketIssuedEvent payload = TicketIssuedEvent.builder()
                .bookingId(aggregate.booking().getId())
                .bookingCode(aggregate.booking().getBookingCode())
                .customerId(aggregate.booking().getCustomerId())
                .customerName(aggregate.booking().getCustomerName())
                .customerPhone(aggregate.booking().getCustomerPhone())
                .customerEmail(aggregate.booking().getCustomerEmail())
                .tripId(aggregate.booking().getTripId())
                .totalAmount(aggregate.booking().getTotalAmount())
                .currency(aggregate.booking().getCurrency())
                .paidAt(paidAt)
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

        outBoxService.generateEvent(
                aggregate.booking().getId(),
                bookingTopic,
                ticketIssuedEvent,
                ticketIssuedEvent,
                payload,
                context
        );
    }
}

