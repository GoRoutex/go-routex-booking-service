package vn.com.routex.hub.booking.service.infrastructure.persistence.adapter.ticket;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.domain.common.PagedResult;
import vn.com.routex.hub.booking.service.domain.ticket.model.Ticket;
import vn.com.routex.hub.booking.service.domain.ticket.port.TicketRepositoryPort;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.ticket.repository.TicketEntityRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TicketRepositoryAdapter implements TicketRepositoryPort {

    private final TicketEntityRepository ticketEntityRepository;
    private final TicketPersistenceMapper ticketPersistenceMapper;

    @Override
    public String generateTicketCode() {
        String ticketCode;
        do {
            ticketCode = "TKT-" + OffsetDateTime.now().toEpochSecond() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (ticketEntityRepository.existsByTicketCode(ticketCode));
        return ticketCode;
    }

    @Override
    public List<Ticket> saveAll(List<Ticket> tickets) {
        return ticketEntityRepository.saveAll(
                        tickets.stream()
                                .map(ticketPersistenceMapper::toEntity)
                                .toList())
                .stream()
                .map(ticketPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public PagedResult<Ticket> fetchByCustomerId(String customerId, int pageNumber, int pageSize) {
        Page<vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.ticket.entity.TicketEntity> page =
                ticketEntityRepository.findByCustomerId(customerId, PageRequest.of(pageNumber, pageSize));

        return PagedResult.<Ticket>builder()
                .items(page.getContent().stream().map(ticketPersistenceMapper::toDomain).toList())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    public Optional<Ticket> findByIdAndCustomerId(String ticketId, String customerId) {
        return ticketEntityRepository.findByIdAndCustomerId(ticketId, customerId)
                .map(ticketPersistenceMapper::toDomain);
    }
}
