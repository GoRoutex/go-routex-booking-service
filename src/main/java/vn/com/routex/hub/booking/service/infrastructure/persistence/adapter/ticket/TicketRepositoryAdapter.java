package vn.com.routex.hub.booking.service.infrastructure.persistence.adapter.ticket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.domain.ticket.port.TicketRepositoryPort;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.ticket.repository.TicketEntityRepository;

@Component
@RequiredArgsConstructor
public class TicketRepositoryAdapter implements TicketRepositoryPort {

    private final TicketEntityRepository ticketEntityRepository;
    @Override
    public String generateTicketCode() {
        return "";
    }
}
