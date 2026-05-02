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
import vn.com.routex.hub.booking.service.application.services.BookingPaymentQueryService;
import vn.com.routex.hub.booking.service.domain.booking.model.Booking;
import vn.com.routex.hub.booking.service.infrastructure.persistence.utils.HttpUtils;
import vn.com.routex.hub.booking.service.interfaces.models.base.BaseRequest;
import vn.com.routex.hub.booking.service.interfaces.models.booking.FetchBookingPaymentContextRequest;
import vn.com.routex.hub.booking.service.interfaces.models.booking.FetchBookingPaymentContextResponse;
import vn.com.routex.hub.booking.service.interfaces.models.result.ApiResult;

import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ApiConstant.BOOKING_PATH;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_CODE;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_MESSAGE;

@RestController
@RequestMapping(API_PATH + API_VERSION + BOOKING_PATH + "/payments")
@RequiredArgsConstructor
public class BookingPaymentQueryController {

    private final BookingPaymentQueryService bookingPaymentQueryService;

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder, WebRequest webRequest) {
        webDataBinder.setDisallowedFields("requestId", "requestDateTime", "channel", "data");
    }

    @PostMapping("/context")
    public ResponseEntity<FetchBookingPaymentContextResponse> fetchBookingPaymentContext(
            @Valid @RequestBody FetchBookingPaymentContextRequest request
    ) {
        Booking booking = bookingPaymentQueryService.getBookingPaymentContext(
                request.getData().getBookingCode(),
                toContext(request)
        );

        FetchBookingPaymentContextResponse response = FetchBookingPaymentContextResponse.builder()
                .result(ApiResult.builder()
                        .responseCode(SUCCESS_CODE)
                        .description(SUCCESS_MESSAGE)
                        .build())
                .data(FetchBookingPaymentContextResponse.Data.builder()
                        .bookingId(booking.getId())
                        .bookingCode(booking.getBookingCode())
                        .customerId(booking.getCustomerId())
                        .totalAmount(booking.getTotalAmount())
                        .currency(booking.getCurrency())
                        .bookingStatus(booking.getStatus())
                        .holdUntil(booking.getHoldUntil())
                        .build())
                .build();

        return HttpUtils.buildResponse(request, response);
    }

    private RequestContext toContext(BaseRequest request) {
        return RequestContext.builder()
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .build();
    }
}
