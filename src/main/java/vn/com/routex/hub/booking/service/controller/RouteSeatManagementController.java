package vn.com.routex.hub.booking.service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.routex.hub.booking.service.application.dto.common.RequestMetadata;
import vn.com.routex.hub.booking.service.application.dto.seat.GetAllSeatQuery;
import vn.com.routex.hub.booking.service.application.dto.seat.GetAllSeatResult;
import vn.com.routex.hub.booking.service.application.dto.seat.HoldSeatCommand;
import vn.com.routex.hub.booking.service.application.dto.seat.HoldSeatResult;
import vn.com.routex.hub.booking.service.application.dto.seat.SeatItemResult;
import vn.com.routex.hub.booking.service.application.services.GetAllSeatService;
import vn.com.routex.hub.booking.service.application.services.HoldSeatService;
import vn.com.routex.hub.booking.service.controller.models.base.BaseRequest;
import vn.com.routex.hub.booking.service.controller.models.result.ApiResult;
import vn.com.routex.hub.booking.service.controller.models.seat.GetAllSeatRequest;
import vn.com.routex.hub.booking.service.controller.models.seat.GetAllSeatResponse;
import vn.com.routex.hub.booking.service.controller.models.seat.HoldSeatRequest;
import vn.com.routex.hub.booking.service.controller.models.seat.HoldSeatResponse;
import vn.com.routex.hub.booking.service.infrastructure.persistence.utils.HttpResponseUtil;

import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ApiConstant.HOLD_SEAT_PATH;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ApiConstant.MANAGEMENT_PATH;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ApiConstant.ROUTE_SERVICE;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ApiConstant.SEAT_PATH;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_CODE;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_MESSAGE;

@RestController
@RequestMapping(API_PATH + API_VERSION + MANAGEMENT_PATH)
@RequiredArgsConstructor
public class RouteSeatManagementController {

    private final GetAllSeatService getAllSeatService;
    private final HoldSeatService holdSeatService;

    @PostMapping(ROUTE_SERVICE + SEAT_PATH)
    public ResponseEntity<GetAllSeatResponse> getAvailableSeat(@Valid @RequestBody GetAllSeatRequest request) {
        GetAllSeatResult result = getAllSeatService.getAllSeat(GetAllSeatQuery.builder()
                .metadata(toMetadata(request))
                .routeId(request.getData().getRouteId())
                .build());

        GetAllSeatResponse response = new GetAllSeatResponse();
        response.setResult(successResult());
        response.setData(result.seats().stream().map(this::toSeatResponse).toList());

        return HttpResponseUtil.buildResponse(request, response);
    }

    @PostMapping(ROUTE_SERVICE + HOLD_SEAT_PATH)
    public ResponseEntity<HoldSeatResponse> holdSeat(@Valid @RequestBody HoldSeatRequest request) {
        HoldSeatResult result = holdSeatService.holdSeat(HoldSeatCommand.builder()
                .metadata(toMetadata(request))
                .routeId(request.getData().getRouteId())
                .vehicleId(request.getData().getVehicleId())
                .seatNos(request.getData().getSeatNos())
                .holdBy(request.getData().getHoldBy())
                .customerId(request.getInfo().getCustomerId())
                .customerName(request.getInfo().getCustomerName())
                .customerPhone(request.getInfo().getCustomerPhone())
                .customerEmail(request.getInfo().getCustomerEmail())
                .currency(request.getInfo().getCurrency())
                .build());

        java.util.List<HoldSeatResponse.HoldSeatResponseData> responseData = result.seats().stream()
                .map(item -> {
                    HoldSeatResponse.HoldSeatResponseBookingInfo bookingInfo = new HoldSeatResponse.HoldSeatResponseBookingInfo();
                    bookingInfo.setBookingId(item.booking().bookingId());
                    bookingInfo.setBookingCode(item.booking().bookingCode());
                    bookingInfo.setHoldUntil(item.booking().holdUntil());
                    bookingInfo.setSeatCount(item.booking().seatCount());
                    bookingInfo.setTotalAmount(item.booking().totalAmount());
                    bookingInfo.setCurrency(item.booking().currency());

                    HoldSeatResponse.HoldSeatResponseData data = new HoldSeatResponse.HoldSeatResponseData();
                    data.setRouteId(item.routeId());
                    data.setSeatNo(item.seatNo());
                    data.setStatus(item.status());
                    data.setHoldToken(item.holdToken());
                    data.setBooking(bookingInfo);
                    return data;
                })
                .toList();

        HoldSeatResponse response = new HoldSeatResponse();
        response.setResult(successResult());
        response.setData(responseData);

        return HttpResponseUtil.buildResponse(request, response);
    }

    private RequestMetadata toMetadata(BaseRequest request) {
        return RequestMetadata.builder()
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .build();
    }

    private GetAllSeatResponse.GetAvailableSeatResponseData toSeatResponse(SeatItemResult item) {
        GetAllSeatResponse.GetAvailableSeatResponseData data = new GetAllSeatResponse.GetAvailableSeatResponseData();
        data.setRouteId(item.routeId());
        data.setSeatNo(item.seatNo());
        data.setStatus(item.status());
        return data;
    }

    private ApiResult successResult() {
        return ApiResult.builder()
                .responseCode(SUCCESS_CODE)
                .description(SUCCESS_MESSAGE)
                .build();
    }
}
