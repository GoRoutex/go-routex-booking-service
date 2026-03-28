package vn.com.routex.hub.booking.service.domain.ticket.port;

/**
 * Domain repository port (no Spring Data/JPA dependency).
 */
public interface TicketRepositoryPort {
    String generateTicketCode();
}
