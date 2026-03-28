package vn.com.routex.hub.booking.service.infrastructure.persistence.adapter.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.domain.booking.model.BookingSeat;
import vn.com.routex.hub.booking.service.domain.booking.port.BookingSeatRepositoryPort;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.booking.repository.BookingSeatEntityRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BookingSeatRepositoryAdapter implements BookingSeatRepositoryPort {

    private final BookingSeatEntityRepository bookingSeatJpaRepository;
    private final BookingPersistenceMapper bookingPersistenceMapper;

    @Override
    public List<BookingSeat> saveAll(List<BookingSeat> bookingSeats) {
        return bookingSeatJpaRepository.saveAll(bookingSeats.stream()
                        .map(bookingPersistenceMapper::toJpaEntity)
                        .toList()).stream()
                .map(bookingPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public BookingSeat save(BookingSeat bookingSeat) {
        return bookingPersistenceMapper.toDomain(
                bookingSeatJpaRepository.save(bookingPersistenceMapper.toJpaEntity(bookingSeat))
        );
    }

    @Override
    public Optional<BookingSeat> findByBookingId(String bookingId) {
        return bookingSeatJpaRepository.findByBookingId(bookingId)
                .map(bookingPersistenceMapper::toDomain);
    }
}
