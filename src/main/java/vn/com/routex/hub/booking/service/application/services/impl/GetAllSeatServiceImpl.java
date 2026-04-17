package vn.com.routex.hub.booking.service.application.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.booking.service.application.dto.seat.GetAllSeatQuery;
import vn.com.routex.hub.booking.service.application.dto.seat.GetAllSeatResult;
import vn.com.routex.hub.booking.service.application.dto.seat.SeatItemResult;
import vn.com.routex.hub.booking.service.application.services.GetAllSeatService;
import vn.com.routex.hub.booking.service.domain.route.model.RouteAggregate;
import vn.com.routex.hub.booking.service.domain.route.port.RouteAggregateRepositoryPort;
import vn.com.routex.hub.booking.service.domain.seat.model.RouteSeat;
import vn.com.routex.hub.booking.service.domain.seat.port.RouteSeatRepositoryPort;
import vn.com.routex.hub.booking.service.infrastructure.cache.redis.models.RouteCacheSeat;
import vn.com.routex.hub.booking.service.infrastructure.cache.redis.service.RouteSeatCacheService;
import vn.com.routex.hub.booking.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.booking.service.infrastructure.persistence.log.SystemLog;
import vn.com.routex.hub.booking.service.infrastructure.persistence.utils.ExceptionUtils;

import java.util.List;

import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.ROUTE_NOT_FOUND;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.ROUTE_SEAT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class GetAllSeatServiceImpl implements GetAllSeatService {

    private final RouteSeatRepositoryPort routeSeatRepositoryPort;
    private final RouteAggregateRepositoryPort routeAggregateRepositoryPort;
    private final RouteSeatCacheService routeSeatCacheService;

    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @Override
    public GetAllSeatResult getAllSeat(GetAllSeatQuery query) {
        sLog.info("[BOOK-SERVICE] Get All Seat Query: {}", query);
        ensureRouteExists(query);
        List<RouteCacheSeat> cacheSeats = routeSeatCacheService.getSeats(query.routeId());
        if (cacheSeats != null && !cacheSeats.isEmpty()) {
            return GetAllSeatResult.builder()
                    .seats(cacheSeats.stream()
                            .map(seat -> SeatItemResult.builder()
                                    .routeId(seat.routeId())
                                    .seatNo(seat.seatNo())
                                    .status(seat.status())
                                    .build())
                            .toList())
                    .build();
        }

        List<RouteSeat> routeSeatList = routeSeatRepositoryPort.findAllByRouteIdOrderBySeatNoAsc(query.routeId());
        if (routeSeatList.isEmpty()) {
            throw new BusinessException(
                    query.metadata().requestId(),
                    query.metadata().requestDateTime(),
                    query.metadata().channel(),
                    ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(ROUTE_SEAT_NOT_FOUND, query.routeId()))
            );
        }

        routeSeatCacheService.putSeats(query.routeId(), routeSeatList.stream()
                .map(seat -> RouteCacheSeat.builder()
                        .routeId(seat.getRouteId())
                        .seatNo(seat.getSeatNo())
                        .status(seat.getStatus())
                        .build())
                .toList());

        return GetAllSeatResult.builder()
                .seats(routeSeatList.stream()
                        .map(seat -> SeatItemResult.builder()
                                .routeId(seat.getRouteId())
                                .seatNo(seat.getSeatNo())
                                .status(seat.getStatus())
                                .build())
                        .toList())
                .build();
    }

    private RouteAggregate ensureRouteExists(GetAllSeatQuery query) {
        return routeAggregateRepositoryPort.findById(query.routeId())
                .orElseThrow(() -> new BusinessException(
                        query.metadata().requestId(),
                        query.metadata().requestDateTime(),
                        query.metadata().channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(ROUTE_NOT_FOUND, query.routeId()))
                ));
    }
}
