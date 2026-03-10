package vn.com.routex.hub.booking.service.application.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.booking.service.application.services.RouteSeatManagementService;
import vn.com.routex.hub.booking.service.domain.seat.RouteSeat;
import vn.com.routex.hub.booking.service.domain.seat.RouteSeatRepository;
import vn.com.routex.hub.booking.service.domain.seat.SeatStatus;
import vn.com.routex.hub.booking.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.booking.service.infrastructure.persistence.utils.ExceptionUtils;
import vn.com.routex.hub.booking.service.interfaces.models.result.ApiResult;
import vn.com.routex.hub.booking.service.interfaces.models.seat.GetAllSeatRequest;
import vn.com.routex.hub.booking.service.interfaces.models.seat.GetAllSeatResponse;
import vn.com.routex.hub.booking.service.interfaces.models.seat.HoldSeatRequest;
import vn.com.routex.hub.booking.service.interfaces.models.seat.HoldSeatResponse;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
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
    @Override
    public GetAllSeatResponse getAllSeat(GetAllSeatRequest request) {

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
                        .ticketId(rs.getTicketId())
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
    public HoldSeatResponse holdSeat(HoldSeatRequest request) {
        String routeId = request.getData().getRouteId();
        List<String> requestedSeatNos = request.getData().getSeatNos();
        String holdBy = request.getData().getHoldBy();

        if(requestedSeatNos == null || requestedSeatNos.isEmpty()) {
            throw new BusinessException(request.getRequestId(),
                     request.getRequestDateTime(), request.getChannel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_SEAT_NO));
        }

        List<String> distinctSeatNos = requestedSeatNos.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .distinct()
                .toList();

        if(distinctSeatNos.isEmpty()) {
            throw new BusinessException(request.getRequestId(), request.getRequestDateTime(), request.getChannel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_SEAT_NO));
        }

        List<RouteSeat> routeSeats = routeSeatRepository.findAllByRouteIdAndSeatNoIn(routeId, distinctSeatNos);

        if(routeSeats.size() != distinctSeatNos.size()) {
            throw new BusinessException(request.getRequestId(), request.getRequestDateTime(), request.getChannel(),
                    ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, SEAT_NOT_FOUND));
        }

        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime holdUntil = now.plusMinutes(5);

        for (RouteSeat seat : routeSeats) {
            if(SeatStatus.HELD.equals(seat.getStatus())
                        && seat.getHoldUntil() != null
                        && seat.getHoldUntil().isBefore(now)) {
                seat.setStatus(SeatStatus.AVAILABLE);
                seat.setHoldUntil(null);
                seat.setHoldBy(null);
            }

            if(!SeatStatus.AVAILABLE.equals(seat.getStatus())) {
                throw new BusinessException(request.getRequestId(), request.getRequestDateTime(), request.getChannel(),
                        ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, String.format(SEAT_NOT_AVAILABLE, seat.getSeatNo())));
            }
        }

        for (RouteSeat seat : routeSeats) {
            seat.setStatus(SeatStatus.HELD);
            seat.setHoldUntil(holdUntil);
            seat.setHoldBy(holdBy);
        }

        routeSeatRepository.saveAll(routeSeats);

        List<HoldSeatResponse.HoldSeatResponseData> responseData = routeSeats.stream()
                .sorted(Comparator.comparing(RouteSeat::getSeatNo))
                .map(seat -> HoldSeatResponse.HoldSeatResponseData.builder()
                        .routeId(seat.getRouteId())
                        .seatNo(seat.getSeatNo())
                        .status(seat.getStatus().name())
                        .holdUntil(seat.getHoldUntil())
                        .holdBy(seat.getHoldBy())
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
