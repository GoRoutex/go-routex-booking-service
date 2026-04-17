package vn.com.routex.hub.booking.service.infrastructure.persistence.adapter.ticket;


import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.domain.ticket.model.Ticket;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.ticket.entity.TicketEntity;

@Component
public class TicketPersistenceMapper {

    public Ticket toDomain(TicketEntity entity) {
        return Ticket.builder()
                .id(entity.getId())
                .ticketCode(entity.getTicketCode())
                .bookingId(entity.getBookingId())
                .bookingSeatId(entity.getBookingSeatId())
                .routeId(entity.getRouteId())
                .seatNumber(entity.getSeatNumber())
                .customerName(entity.getCustomerName())
                .customerPhone(entity.getCustomerPhone())
                .price(entity.getPrice())
                .status(entity.getStatus())
                .issuedAt(entity.getIssuedAt())
                .checkedInAt(entity.getCheckedInAt())
                .boardedAt(entity.getBoardedAt())
                .cancelledAt(entity.getCancelledAt())
                .checkedInBy(entity.getCheckedInBy())
                .boardedBy(entity.getBoardedBy())
                .cancelledBy(entity.getCancelledBy())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public TicketEntity toEntity(Ticket domain) {
        return TicketEntity.builder()
                .id(domain.getId())
                .ticketCode(domain.getTicketCode())
                .bookingId(domain.getBookingId())
                .bookingSeatId(domain.getBookingSeatId())
                .routeId(domain.getRouteId())
                .seatNumber(domain.getSeatNumber())
                .customerName(domain.getCustomerName())
                .customerPhone(domain.getCustomerPhone())
                .price(domain.getPrice())
                .status(domain.getStatus())
                .issuedAt(domain.getIssuedAt())
                .checkedInAt(domain.getCheckedInAt())
                .boardedAt(domain.getBoardedAt())
                .cancelledAt(domain.getCancelledAt())
                .checkedInBy(domain.getCheckedInBy())
                .boardedBy(domain.getBoardedBy())
                .cancelledBy(domain.getCancelledBy())
                .createdAt(domain.getCreatedAt())
                .createdBy(domain.getCreatedBy())
                .updatedAt(domain.getUpdatedAt())
                .updatedBy(domain.getUpdatedBy())
                .build();
    }
}
