package vn.com.routex.hub.booking.service.infrastructure.persistence.adapter.booking;

import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.domain.booking.model.Booking;
import vn.com.routex.hub.booking.service.domain.booking.model.BookingSeat;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.booking.entity.BookingJpaEntity;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.booking.entity.BookingSeatJpaEntity;

@Component
public class BookingPersistenceMapper {

    public Booking toDomain(BookingJpaEntity entity) {
        return Booking.builder()
                .id(entity.getId())
                .bookingCode(entity.getBookingCode())
                .routeId(entity.getRouteId())
                .customerId(entity.getCustomerId())
                .seatCount(entity.getSeatCount())
                .totalAmount(entity.getTotalAmount())
                .currency(entity.getCurrency())
                .status(entity.getStatus())
                .heldAt(entity.getHeldAt())
                .holdUntil(entity.getHoldUntil())
                .cancelledAt(entity.getCancelledAt())
                .note(entity.getNote())
                .creator(entity.getCreator())
                .build();
    }

    public BookingJpaEntity toJpaEntity(Booking booking) {
        return BookingJpaEntity.builder()
                .id(booking.getId())
                .bookingCode(booking.getBookingCode())
                .routeId(booking.getRouteId())
                .customerId(booking.getCustomerId())
                .seatCount(booking.getSeatCount())
                .totalAmount(booking.getTotalAmount())
                .currency(booking.getCurrency())
                .status(booking.getStatus())
                .heldAt(booking.getHeldAt())
                .holdUntil(booking.getHoldUntil())
                .cancelledAt(booking.getCancelledAt())
                .note(booking.getNote())
                .creator(booking.getCreator())
                .build();
    }

    public BookingSeat toDomain(BookingSeatJpaEntity entity) {
        return BookingSeat.builder()
                .id(entity.getId())
                .bookingId(entity.getBookingId())
                .routeId(entity.getRouteId())
                .seatNo(entity.getSeatNo())
                .price(entity.getPrice())
                .status(entity.getStatus())
                .creator(entity.getCreator())
                .build();
    }

    public BookingSeatJpaEntity toJpaEntity(BookingSeat bookingSeat) {
        return BookingSeatJpaEntity.builder()
                .id(bookingSeat.getId())
                .bookingId(bookingSeat.getBookingId())
                .routeId(bookingSeat.getRouteId())
                .seatNo(bookingSeat.getSeatNo())
                .price(bookingSeat.getPrice())
                .status(bookingSeat.getStatus())
                .creator(bookingSeat.getCreator())
                .build();
    }
}
