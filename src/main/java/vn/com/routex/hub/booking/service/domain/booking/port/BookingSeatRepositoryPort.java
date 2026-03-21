package vn.com.routex.hub.booking.service.domain.booking.port;

import vn.com.routex.hub.booking.service.domain.booking.model.BookingSeat;

import java.util.List;
import java.util.Optional;

public interface BookingSeatRepositoryPort {
    List<BookingSeat> saveAll(List<BookingSeat> bookingSeats);

    BookingSeat save(BookingSeat bookingSeat);

    Optional<BookingSeat> findByBookingId(String bookingId);
}
