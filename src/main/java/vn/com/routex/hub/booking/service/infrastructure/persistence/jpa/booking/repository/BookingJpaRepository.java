package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.booking.entity.BookingJpaEntity;

public interface BookingJpaRepository extends JpaRepository<BookingJpaEntity, String> {

    @Query(value = """
            SELECT generate_booking_code()
            """, nativeQuery = true)
    String generateBookingCode();
}
