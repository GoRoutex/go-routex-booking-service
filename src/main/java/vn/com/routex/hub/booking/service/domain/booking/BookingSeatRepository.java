package vn.com.routex.hub.booking.service.domain.booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingSeatRepository extends JpaRepository<BookingSeat, String> {
    Optional<BookingSeat> findByBookingId(String id);
}
