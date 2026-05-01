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
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.hub.booking.service.application.command.seat.HoldSeatCommand;
import vn.com.routex.hub.booking.service.application.command.seat.HoldSeatResult;
import vn.com.routex.hub.booking.service.application.services.HoldSeatService;
import vn.com.routex.hub.booking.service.infrastructure.persistence.utils.HttpUtils;
import vn.com.routex.hub.booking.service.interfaces.models.result.ApiResult;
import vn.com.routex.hub.booking.service.interfaces.models.seat.HoldSeatRequest;
import vn.com.routex.hub.booking.service.interfaces.models.seat.HoldSeatResponse;

import java.util.List;
import java.util.stream.Collectors;

import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ApiConstant.BOOKING_PATH;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ApiConstant.HOLD_SEAT_PATH;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ApiConstant.TRIP_PATH;

@RestController
@RequestMapping(API_PATH + API_VERSION + BOOKING_PATH)
@RequiredArgsConstructor
public class RouteSeatManagementController {

    private final HoldSeatService holdSeatService;
    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder, WebRequest webRequest) {
        webDataBinder.setDisallowedFields("requestId", "requestDateTime", "channel", "data");
    }

    @PostMapping(TRIP_PATH + HOLD_SEAT_PATH)
    public ResponseEntity<HoldSeatResponse> holdSeat(@Valid @RequestBody HoldSeatRequest request) {
        sLog.info("[HOLD-SEAT] Hold Seat Request: {}", request);

        HoldSeatResult result = holdSeatService.holdSeat(HoldSeatCommand.builder()
                .context(HttpUtils.toContext(request))
                .routeId(request.getData().getRouteId())
                .seatNos(request.getData().getSeatNos())
                .holdBy(request.getData().getHoldBy())
                .customerName(request.getInfo().getCustomerName())
                .customerPhone(request.getInfo().getCustomerPhone())
                .customerEmail(request.getInfo().getCustomerEmail())
                .build());

        List<HoldSeatResponse.HoldSeatResponseData> responseData = result.seats().stream()
                .map(item -> {
                    return HoldSeatResponse.HoldSeatResponseData.builder()
                            .routeId(item.routeId())
                            .seatNo(item.seatNo())
                            .status(item.status())
                            .holdToken(item.holdToken())
                            .build();
                })
                .collect(Collectors.toList());

        HoldSeatResponse.HoldSeatResponseBookingInfo bookingInfo = getHoldSeatResponseBookingInfo(result);

        HoldSeatResponse response = HoldSeatResponse.builder()
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .result(ApiResult.buildSuccess())
                .data(responseData)
                .booking(bookingInfo)
                .build();


        sLog.info("[HOLD-SEAT] Hold Seat Response: {}", response);

        return HttpUtils.buildResponse(request, response);
    }

    private static HoldSeatResponse.HoldSeatResponseBookingInfo getHoldSeatResponseBookingInfo(HoldSeatResult item) {
        return HoldSeatResponse.HoldSeatResponseBookingInfo.builder()
                .bookingId(item.booking().bookingId())
                .bookingCode(item.booking().bookingCode())
                .holdUntil(item.booking().holdUntil())
                .seatCount(item.booking().seatCount())
                .totalAmount(item.booking().totalAmount())
                .currency(item.booking().currency())
                .build();

    }
}
