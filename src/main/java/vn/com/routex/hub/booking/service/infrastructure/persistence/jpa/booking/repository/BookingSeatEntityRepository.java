package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.booking.entity.BookingSeatEntity;

import java.util.Optional;

public interface BookingSeatEntityRepository extends JpaRepository<BookingSeatEntity, String> {
    Optional<BookingSeatEntity> findByBookingId(String bookingId);
}
