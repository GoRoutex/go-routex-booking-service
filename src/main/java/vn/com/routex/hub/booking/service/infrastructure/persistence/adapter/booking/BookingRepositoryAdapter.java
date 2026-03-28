package vn.com.routex.hub.booking.service.infrastructure.persistence.adapter.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.domain.booking.model.Booking;
import vn.com.routex.hub.booking.service.domain.booking.port.BookingRepositoryPort;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.booking.repository.BookingEntityRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BookingRepositoryAdapter implements BookingRepositoryPort {

    private final BookingEntityRepository bookingJpaRepository;
    private final BookingPersistenceMapper bookingPersistenceMapper;

    @Override
    public Booking save(Booking booking) {
        return bookingPersistenceMapper.toDomain(
                bookingJpaRepository.save(bookingPersistenceMapper.toJpaEntity(booking))
        );
    }

    @Override
    public Optional<Booking> findById(String bookingId) {
        return bookingJpaRepository.findById(bookingId).map(bookingPersistenceMapper::toDomain);
    }

    @Override
    public String generateBookingCode() {
        return bookingJpaRepository.generateBookingCode();
    }
}
