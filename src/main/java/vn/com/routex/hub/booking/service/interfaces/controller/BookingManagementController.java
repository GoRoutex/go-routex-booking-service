package vn.com.routex.hub.booking.service.interfaces.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import vn.com.routex.hub.booking.service.application.command.common.RequestContext;
import vn.com.routex.hub.booking.service.application.command.ticket.FetchTicketDetailQuery;
import vn.com.routex.hub.booking.service.application.command.ticket.FetchTicketDetailResult;
import vn.com.routex.hub.booking.service.application.command.ticket.FetchTicketsQuery;
import vn.com.routex.hub.booking.service.application.command.ticket.FetchTicketsResult;
import vn.com.routex.hub.booking.service.application.services.TicketQueryService;
import vn.com.routex.hub.booking.service.interfaces.models.base.BaseRequest;
import vn.com.routex.hub.booking.service.interfaces.models.result.ApiResult;
import vn.com.routex.hub.booking.service.interfaces.models.ticket.FetchTicketDetailRequest;
import vn.com.routex.hub.booking.service.interfaces.models.ticket.FetchTicketDetailResponse;
import vn.com.routex.hub.booking.service.interfaces.models.ticket.FetchTicketsRequest;
import vn.com.routex.hub.booking.service.interfaces.models.ticket.FetchTicketsResponse;
import vn.com.routex.hub.booking.service.infrastructure.persistence.utils.HttpUtils;

import java.util.List;

import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ApiConstant.BOOKING_PATH;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ApiConstant.DETAIL_PATH;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ApiConstant.FETCH_PATH;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ApiConstant.TICKET_PATH;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_CODE;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_MESSAGE;

@RestController
@RequestMapping(API_PATH + API_VERSION + BOOKING_PATH)
@RequiredArgsConstructor
public class BookingManagementController {

    private final TicketQueryService ticketQueryService;

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder, WebRequest webRequest) {
        webDataBinder.setDisallowedFields("requestId", "requestDateTime", "channel", "data");
    }

    @PostMapping(TICKET_PATH + FETCH_PATH)
    public ResponseEntity<FetchTicketsResponse> fetchTickets(@Valid @RequestBody FetchTicketsRequest request) {
        FetchTicketsResult result = ticketQueryService.fetchTickets(FetchTicketsQuery.builder()
                .metadata(toMetadata(request))
                .customerId(request.getData().getCustomerId())
                .pageNumber(request.getData().getPageNumber())
                .pageSize(request.getData().getPageSize())
                .build());

        List<FetchTicketsResponse.FetchTicketsResponseData> items = result.items().stream()
                .map(item -> new FetchTicketsResponse.FetchTicketsResponseData(
                        item.id(),
                        item.ticketCode(),
                        item.bookingId(),
                        item.bookingSeatId(),
                        item.routeId(),
                        item.seatNumber(),
                        item.customerName(),
                        item.customerPhone(),
                        item.price(),
                        item.status(),
                        item.issuedAt()))
                .toList();

        FetchTicketsResponse response = FetchTicketsResponse.builder()
                .result(successResult())
                .data(FetchTicketsResponse.FetchTicketsResponsePage.builder()
                        .items(items)
                        .pagination(FetchTicketsResponse.Pagination.builder()
                                .pageNumber(result.pageNumber())
                                .pageSize(result.pageSize())
                                .totalElements(result.totalElements())
                                .totalPages(result.totalPages())
                                .build())
                        .build())
                .build();

        return HttpUtils.buildResponse(request, response);
    }

    @PostMapping(TICKET_PATH + DETAIL_PATH)
    public ResponseEntity<FetchTicketDetailResponse> fetchTicketDetail(@Valid @RequestBody FetchTicketDetailRequest request) {
        FetchTicketDetailResult result = ticketQueryService.fetchTicketDetail(FetchTicketDetailQuery.builder()
                .metadata(toMetadata(request))
                .customerId(request.getData().getCustomerId())
                .ticketId(request.getData().getTicketId())
                .build());

        FetchTicketDetailResponse response = FetchTicketDetailResponse.builder()
                .result(successResult())
                .data(FetchTicketDetailResponse.FetchTicketDetailResponseData.builder()
                        .id(result.id())
                        .ticketCode(result.ticketCode())
                        .bookingId(result.bookingId())
                        .bookingSeatId(result.bookingSeatId())
                        .routeId(result.routeId())
                        .seatNumber(result.seatNumber())
                        .customerName(result.customerName())
                        .customerPhone(result.customerPhone())
                        .price(result.price())
                        .status(result.status())
                        .issuedAt(result.issuedAt())
                        .checkedInAt(result.checkedInAt())
                        .boardedAt(result.boardedAt())
                        .cancelledAt(result.cancelledAt())
                        .checkedInBy(result.checkedInBy())
                        .boardedBy(result.boardedBy())
                        .cancelledBy(result.cancelledBy())
                        .build())
                .build();

        return HttpUtils.buildResponse(request, response);
    }

    private RequestContext toMetadata(BaseRequest request) {
        return RequestContext.builder()
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .build();
    }

    private ApiResult successResult() {
        return ApiResult.builder()
                .responseCode(SUCCESS_CODE)
                .description(SUCCESS_MESSAGE)
                .build();
    }
}
