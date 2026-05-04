package vn.com.routex.hub.booking.service.infrastructure.persistence.adapter.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.domain.booking.BookingStatus;
import vn.com.routex.hub.booking.service.domain.booking.model.Booking;
import vn.com.routex.hub.booking.service.domain.booking.port.BookingRepositoryPort;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.booking.repository.BookingEntityRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BookingRepositoryAdapter implements BookingRepositoryPort {

    private final BookingEntityRepository bookingEntityRepository;
    private final BookingPersistenceMapper bookingPersistenceMapper;

    @Override
    public Booking save(Booking booking) {
        return bookingPersistenceMapper.toDomain(
                bookingEntityRepository.save(bookingPersistenceMapper.toEntity(booking))
        );
    }

    @Override
    public Optional<Booking> findById(String bookingId) {
        return bookingEntityRepository.findById(bookingId).map(bookingPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Booking> findByBookingCodeForUpdate(String bookingCode) {
        return bookingEntityRepository.findByBookingCodeForUpdate(bookingCode).map(bookingPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Booking> findByBookingCode(String bookingCode) {
        return bookingEntityRepository.findByBookingCode(bookingCode).map(bookingPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Booking> findById(String bookingId, String merchantId) {
        return bookingEntityRepository.findByIdAndMerchantId(bookingId, merchantId).map(bookingPersistenceMapper::toDomain);
    }

    @Override
    public List<Booking> findExpiredPendingPaymentBookingsForUpdate(OffsetDateTime holdUntil, int limit) {
        return bookingEntityRepository.findExpiredPendingPaymentBookingsForUpdate(
                        holdUntil,
                        PageRequest.of(0, limit),
                        BookingStatus.PENDING_PAYMENT
                ).stream()
                .map(bookingPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public String generateBookingCode() {
        return bookingEntityRepository.generateBookingCode();
    }
}
