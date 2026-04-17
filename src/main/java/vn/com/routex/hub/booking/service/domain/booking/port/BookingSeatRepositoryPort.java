package vn.com.routex.hub.booking.service.domain.booking.port;

import vn.com.routex.hub.booking.service.domain.booking.model.BookingSeat;

import java.util.List;

public interface BookingSeatRepositoryPort {
    List<BookingSeat> saveAll(List<BookingSeat> bookingSeats);

    BookingSeat save(BookingSeat bookingSeat);

    List<BookingSeat> findAllByBookingId(String bookingId);
}
