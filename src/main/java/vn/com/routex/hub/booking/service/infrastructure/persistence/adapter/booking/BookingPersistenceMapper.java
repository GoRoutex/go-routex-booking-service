package vn.com.routex.hub.booking.service.infrastructure.persistence.adapter.booking;

import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.domain.booking.model.Booking;
import vn.com.routex.hub.booking.service.domain.booking.model.BookingSeat;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.booking.entity.BookingEntity;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.booking.entity.BookingSeatEntity;

@Component
public class BookingPersistenceMapper {

    public Booking toDomain(BookingEntity entity) {
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

    public BookingEntity toJpaEntity(Booking booking) {
        return BookingEntity.builder()
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

    public BookingSeat toDomain(BookingSeatEntity entity) {
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

    public BookingSeatEntity toJpaEntity(BookingSeat bookingSeat) {
        return BookingSeatEntity.builder()
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
