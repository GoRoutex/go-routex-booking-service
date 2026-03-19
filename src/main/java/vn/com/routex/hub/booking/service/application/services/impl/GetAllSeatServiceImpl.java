package vn.com.routex.hub.booking.service.application.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.booking.service.application.services.GetAllSeatService;
import vn.com.routex.hub.booking.service.controller.models.result.ApiResult;
import vn.com.routex.hub.booking.service.controller.models.seat.GetAllSeatRequest;
import vn.com.routex.hub.booking.service.controller.models.seat.GetAllSeatResponse;
import vn.com.routex.hub.booking.service.domain.seat.RouteSeat;
import vn.com.routex.hub.booking.service.domain.seat.RouteSeatRepository;
import vn.com.routex.hub.booking.service.infrastructure.cache.redis.models.RouteCacheSeat;
import vn.com.routex.hub.booking.service.infrastructure.cache.redis.service.RouteSeatCacheService;
import vn.com.routex.hub.booking.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.booking.service.infrastructure.persistence.log.SystemLog;
import vn.com.routex.hub.booking.service.infrastructure.persistence.utils.ExceptionUtils;

import java.util.List;
import java.util.stream.Collectors;

import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.ROUTE_SEAT_NOT_FOUND;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_CODE;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_MESSAGE;

@Service
@RequiredArgsConstructor
public class GetAllSeatServiceImpl implements GetAllSeatService {

    private final RouteSeatRepository routeSeatRepository;
    private final RouteSeatCacheService routeSeatCacheService;

    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @Override
    public GetAllSeatResponse getAllSeat(GetAllSeatRequest request) {
        sLog.info("[BOOK-SERVICE] Get All Seat Request: {}", request);
        List<RouteCacheSeat> cacheSeats = routeSeatCacheService.getSeats(request.getData().getRouteId());
        if(cacheSeats != null && !cacheSeats.isEmpty()) {
            List<GetAllSeatResponse.GetAvailableSeatResponseData> seatResponseData = cacheSeats.stream()
                    .map(seat -> GetAllSeatResponse.GetAvailableSeatResponseData.builder()
                            .routeId(seat.routeId())
                            .seatNo(seat.seatNo())
                            .status(seat.status())
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
                    .data(seatResponseData)
                    .build();

        }
        List<RouteSeat> routeSeatList = routeSeatRepository.findAllByRouteIdOrderBySeatNoAsc(request.getData().getRouteId());

        if(routeSeatList.isEmpty()) {
            throw new BusinessException(request.getRequestId(), request.getRequestDateTime(), request.getChannel(),
                    ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(ROUTE_SEAT_NOT_FOUND, request.getData().getRouteId())));
        }

        List<RouteCacheSeat> cacheData = routeSeatList.stream()
                        .map(seat -> RouteCacheSeat.builder()
                                .routeId(seat.getRouteId())
                                .seatNo(seat.getSeatNo())
                                .status(seat.getStatus())
                                .build())
                                .toList();

        routeSeatCacheService.putSeats(request.getData().getRouteId(), cacheData);
        List<GetAllSeatResponse.GetAvailableSeatResponseData> responseDataList = routeSeatList
                .stream()
                .map(rs -> GetAllSeatResponse.GetAvailableSeatResponseData.builder()
                        .routeId(rs.getRouteId())
                        .seatNo(rs.getSeatNo())
                        .status(rs.getStatus())
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
}
