package vn.com.routex.hub.booking.service.domain.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {

    @Query(value = """
            SELECT generate_booking_code()
            """, nativeQuery = true)
    String generateRouteCode();
}
