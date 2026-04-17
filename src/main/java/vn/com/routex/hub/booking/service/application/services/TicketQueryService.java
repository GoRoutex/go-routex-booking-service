package vn.com.routex.hub.booking.service.application.services;

import vn.com.routex.hub.booking.service.application.dto.ticket.FetchTicketDetailQuery;
import vn.com.routex.hub.booking.service.application.dto.ticket.FetchTicketDetailResult;
import vn.com.routex.hub.booking.service.application.dto.ticket.FetchTicketsQuery;
import vn.com.routex.hub.booking.service.application.dto.ticket.FetchTicketsResult;

public interface TicketQueryService {
    FetchTicketsResult fetchTickets(FetchTicketsQuery query);

    FetchTicketDetailResult fetchTicketDetail(FetchTicketDetailQuery query);
}
