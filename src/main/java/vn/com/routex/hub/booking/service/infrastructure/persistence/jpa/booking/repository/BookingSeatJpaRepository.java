package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.booking.entity.BookingSeatJpaEntity;

import java.util.Optional;

public interface BookingSeatJpaRepository extends JpaRepository<BookingSeatJpaEntity, String> {
    Optional<BookingSeatJpaEntity> findByBookingId(String bookingId);
}
