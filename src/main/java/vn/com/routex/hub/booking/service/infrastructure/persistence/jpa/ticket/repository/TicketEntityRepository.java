package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.ticket.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.ticket.entity.TicketEntity;

import java.util.Optional;

@Repository
public interface TicketEntityRepository extends JpaRepository<TicketEntity, String> {
    boolean existsByTicketCode(String ticketCode);

    @Query("""
            select t
            from TicketEntity t
            join BookingEntity b on b.id = t.bookingId
            where b.customerId = :customerId
            order by t.issuedAt desc
            """)
    Page<TicketEntity> findByCustomerId(@Param("customerId") String customerId, Pageable pageable);

    @Query("""
            select t
            from TicketEntity t
            join BookingEntity b on b.id = t.bookingId
            where t.id = :ticketId and b.customerId = :customerId
            """)
    Optional<TicketEntity> findByIdAndCustomerId(@Param("ticketId") String ticketId, @Param("customerId") String customerId);
}
