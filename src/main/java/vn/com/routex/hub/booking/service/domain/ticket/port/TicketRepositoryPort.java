package vn.com.routex.hub.booking.service.domain.ticket.port;

import vn.com.routex.hub.booking.service.domain.common.PagedResult;
import vn.com.routex.hub.booking.service.domain.ticket.model.Ticket;

import java.util.List;
import java.util.Optional;

/**
 * Domain repository port (no Spring Data/JPA dependency).
 */
public interface TicketRepositoryPort {
    String generateTicketCode();

    List<Ticket> saveAll(List<Ticket> tickets);

    PagedResult<Ticket> fetchByCustomerId(String customerId, int pageNumber, int pageSize);

    Optional<Ticket> findByIdAndCustomerId(String ticketId, String customerId);
}
