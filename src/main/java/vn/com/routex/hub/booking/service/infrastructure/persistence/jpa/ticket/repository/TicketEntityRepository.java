package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.ticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.ticket.entity.TicketEntity;

@Repository
public interface TicketEntityRepository extends JpaRepository<TicketEntity, String> {
}

