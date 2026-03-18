package vn.com.routex.hub.booking.service.application.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.hub.booking.service.application.services.BookingService;
import vn.com.routex.hub.booking.service.application.services.RouteSeatManagementService;
import vn.com.routex.hub.booking.service.domain.booking.Booking;
import vn.com.routex.hub.booking.service.domain.seat.RouteSeat;
import vn.com.routex.hub.booking.service.domain.seat.RouteSeatRepository;
import vn.com.routex.hub.booking.service.domain.seat.SeatStatus;
import vn.com.routex.hub.booking.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.booking.service.infrastructure.persistence.utils.ExceptionUtils;
import vn.com.routex.hub.booking.service.controller.models.booking.CreateBookingRequest;
import vn.com.routex.hub.booking.service.controller.models.result.ApiResult;
import vn.com.routex.hub.booking.service.controller.models.seat.GetAllSeatRequest;
import vn.com.routex.hub.booking.service.controller.models.seat.GetAllSeatResponse;
import vn.com.routex.hub.booking.service.controller.models.seat.HoldSeatRequest;
import vn.com.routex.hub.booking.service.controller.models.seat.HoldSeatResponse;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.INVALID_INPUT_ERROR;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.INVALID_SEAT_NO;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.ROUTE_SEAT_NOT_FOUND;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.SEAT_NOT_AVAILABLE;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.SEAT_NOT_FOUND;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_CODE;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_MESSAGE;

@Service
@RequiredArgsConstructor
public class RouteSeatManagementServiceImpl implements RouteSeatManagementService {

    private final RouteSeatRepository routeSeatRepository;
    private final BookingService bookingService;

    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @Override
    public GetAllSeatResponse getAllSeat(GetAllSeatRequest request) {

        sLog.info("[BOOK-SERVICE] Get All Seat Request: {}", request);
        List<RouteSeat> routeSeatList = routeSeatRepository.findAllByRouteIdOrderBySeatNoAsc(request.getData().getRouteId());

        if(routeSeatList.isEmpty()) {
            throw new BusinessException(request.getRequestId(), request.getRequestDateTime(), request.getChannel(),
                    ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(ROUTE_SEAT_NOT_FOUND, request.getData().getRouteId())));
        }
        List<GetAllSeatResponse.GetAvailableSeatResponseData> responseDataList = routeSeatList
                .stream()
                .map(rs -> GetAllSeatResponse.GetAvailableSeatResponseData.builder()
                        .routeId(rs.getRouteId())
                        .seatNo(rs.getSeatNo())
                        .status(rs.getStatus().name())
                        .build())
                .collect(Collectors.toList());

        return GetAllSeatResponse.builder()
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .result(ApiResult.builder()
                        .responseCode(SUCCESS_CODE)
                        .description(SUCCESS_MESSAGE)
                        .build())
                .data(responseDataList)
                .build();
    }

    @Override
    @Transactional
    public HoldSeatResponse holdSeat(HoldSeatRequest request) {
        String routeId = request.getData().getRouteId();
        String holdToken = UUID.randomUUID().toString();


        sLog.info("[BOOK-SERVICE] Hold Seat Request: {}", request);
        List<String> requestedSeatNos = request.getData().getSeatNos();

        if(requestedSeatNos == null || requestedSeatNos.isEmpty()) {
            throw new BusinessException(request.getRequestId(),
                     request.getRequestDateTime(), request.getChannel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_SEAT_NO));
        }
        // Normalize requested Seat Nos
        List<String> distinctSeatNos = requestedSeatNos.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .map(String::toUpperCase)
                .filter(s -> !s.isEmpty())
                .distinct()
                .sorted()
                .toList();

        if(distinctSeatNos.isEmpty()) {
            throw new BusinessException(request.getRequestId(), request.getRequestDateTime(), request.getChannel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_SEAT_NO));
        }

        List<RouteSeat> routeSeats = routeSeatRepository.findAllByRouteIdAndSeatNoInForUpdate(routeId, distinctSeatNos);

        if(routeSeats.size() != distinctSeatNos.size()) {
            throw new BusinessException(request.getRequestId(), request.getRequestDateTime(), request.getChannel(),
                    ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, SEAT_NOT_FOUND));
        }

        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime holdUntil = now.plusMinutes(5);

        for (RouteSeat seat : routeSeats) {
            if(SeatStatus.HELD.equals(seat.getStatus())) {
                seat.setStatus(SeatStatus.AVAILABLE);
            }

            if(!SeatStatus.AVAILABLE.equals(seat.getStatus())) {
                throw new BusinessException(request.getRequestId(), request.getRequestDateTime(), request.getChannel(),
                        ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, String.format(SEAT_NOT_AVAILABLE, seat.getSeatNo())));
            }
        }

        for (RouteSeat seat : routeSeats) {
            seat.setStatus(SeatStatus.HELD);
        }
        routeSeatRepository.saveAll(routeSeats);
        // Create draft Booking for payment methods.

        CreateBookingRequest bookingRequest = CreateBookingRequest
                .builder()
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .data(
                        CreateBookingRequest.CreateBookingRequestData
                                .builder()
                                .routeId(request.getData().getRouteId())
                                .vehicleId(request.getData().getVehicleId())
                                .holdBy(request.getData().getHoldBy())
                                .holdToken(holdToken)
                                .heldAt(now)
                                .holdUntil(holdUntil)
                                .build()
                )
                .info(
                        CreateBookingRequest.CreateBookingRequestInformation
                                .builder()
                                .customerId(request.getInfo().getCustomerId())
                                .customerName(request.getInfo().getCustomerName())
                                .customerPhone(request.getInfo().getCustomerPhone())
                                .customerEmail(request.getInfo().getCustomerEmail())
                                .currency(request.getInfo().getCurrency())
                                .build()
                )
                .build();

        Booking booking = bookingService.createBooking(bookingRequest, routeSeats);

        List<HoldSeatResponse.HoldSeatResponseData> responseData = routeSeats.stream()
                .sorted(Comparator.comparing(RouteSeat::getSeatNo))
                .map(seat -> HoldSeatResponse.HoldSeatResponseData.builder()
                        .routeId(seat.getRouteId())
                        .seatNo(seat.getSeatNo())
                        .status(seat.getStatus().name())
                        .holdToken(holdToken)
                        .booking(HoldSeatResponse.HoldSeatResponseBookingInfo.builder()
                                .bookingId(booking.getId())
                                .bookingCode(booking.getBookingCode())
                                .holdUntil(booking.getHoldUntil())
                                .seatCount(booking.getSeatCount())
                                .totalAmount(booking.getTotalAmount())
                                .currency(booking.getCurrency())
                                .build())
                        .build())
                .collect(Collectors.toList());


        return HoldSeatResponse.builder()
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .result(ApiResult.builder()
                        .responseCode(SUCCESS_CODE)
                        .description(SUCCESS_MESSAGE)
                        .build())
                .data(responseData)
                .build();
    }
}
