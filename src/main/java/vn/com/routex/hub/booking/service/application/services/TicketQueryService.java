package vn.com.routex.hub.booking.service.application.services;

import vn.com.routex.hub.booking.service.application.command.ticket.FetchTicketDetailQuery;
import vn.com.routex.hub.booking.service.application.command.ticket.FetchTicketDetailResult;
import vn.com.routex.hub.booking.service.application.command.ticket.FetchTicketsQuery;
import vn.com.routex.hub.booking.service.application.command.ticket.FetchTicketsResult;

public interface TicketQueryService {
    FetchTicketsResult fetchTickets(FetchTicketsQuery query);

    FetchTicketDetailResult fetchTicketDetail(FetchTicketDetailQuery query);
}
