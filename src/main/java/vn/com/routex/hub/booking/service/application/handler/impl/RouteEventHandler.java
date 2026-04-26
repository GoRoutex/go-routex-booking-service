package vn.com.routex.hub.booking.service.application.handler.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.application.handler.RouteEvent;
import vn.com.routex.hub.booking.service.controller.models.base.BaseRequest;
import vn.com.routex.hub.booking.service.domain.seat.SeatStatus;
import vn.com.routex.hub.booking.service.domain.seat.model.RouteSeat;
import vn.com.routex.hub.booking.service.domain.seat.port.RouteSeatRepositoryPort;
import vn.com.routex.hub.booking.service.domain.vehicle.model.VehicleProfile;
import vn.com.routex.hub.booking.service.domain.vehicle.model.VehicleTemplate;
import vn.com.routex.hub.booking.service.domain.vehicle.port.VehicleRepositoryPort;
import vn.com.routex.hub.booking.service.domain.vehicle.port.VehicleTemplateRepositoryPort;
import vn.com.routex.hub.booking.service.infrastructure.cache.redis.models.RouteCacheSeat;
import vn.com.routex.hub.booking.service.infrastructure.kafka.event.DomainEvent;
import vn.com.routex.hub.booking.service.infrastructure.kafka.event.RouteSeatGeneratedEvent;
import vn.com.routex.hub.booking.service.infrastructure.kafka.event.RouteSellableEvent;
import vn.com.routex.hub.booking.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.booking.service.infrastructure.persistence.log.SystemLog;
import vn.com.routex.hub.booking.service.infrastructure.persistence.utils.ExceptionUtils;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.DUPLICATE_ERROR;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.INVALID_DATA_ERROR;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.ROUTE_SEAT_EXIST;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.VEHICLE_NOT_FOUND;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.VEHICLE_TEMPLATE_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class RouteEventHandler implements RouteEvent {

    private final VehicleRepositoryPort vehicleRepositoryPort;
    private final RouteSeatRepositoryPort routeSeatRepositoryPort;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final VehicleTemplateRepositoryPort vehicleTemplateRepositoryPort;

    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @Override
    @Transactional
    public void generateRouteSeat(DomainEvent event, BaseRequest context, RouteSellableEvent payload) {
        VehicleProfile vehicle = vehicleRepositoryPort.findById(payload.vehicleId())
                .orElseThrow(() -> new BusinessException(context.getRequestId(), context.getRequestDateTime(), context.getChannel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, VEHICLE_NOT_FOUND)));

        VehicleTemplate template = vehicleTemplateRepositoryPort.findById(vehicle.getTemplateId())
                .orElseThrow(() -> new BusinessException(ExceptionUtils.buildResultResponse(VEHICLE_NOT_FOUND, VEHICLE_TEMPLATE_NOT_FOUND)));

        if (routeSeatRepositoryPort.existsByRouteId(payload.routeId())) {
            throw new BusinessException(context.getRequestId(), context.getRequestDateTime(), context.getChannel(),
                    ExceptionUtils.buildResultResponse(DUPLICATE_ERROR, String.format(ROUTE_SEAT_EXIST, payload.routeId())));
        }

        sLog.info("[ROUTE-SEAT] Generate seats routeId={} vehicleId={} seatCapacity={} hasFloor={}",
                payload.routeId(), payload.vehicleId(), template.getSeatCapacity(), template.isHasFloor());

        List<RouteSeat> seats = generateSeatNos(vehicle, template).stream()
                .map(seatNo -> RouteSeat.builder()
                        .id(UUID.randomUUID().toString())
                        .routeId(payload.routeId())
                        .seatNo(seatNo)
                        .status(SeatStatus.AVAILABLE)
                        .creator(payload.creator())
                        .createdAt(OffsetDateTime.now())
                        .createdBy(payload.creator())
                        .build())
                .collect(Collectors.toList());

        List<RouteSeat> savedSeats = routeSeatRepositoryPort.saveAll(seats);

        List<RouteCacheSeat> cacheData = savedSeats.stream()
                .map(seat -> RouteCacheSeat.builder()
                        .routeId(seat.getRouteId())
                        .seatNo(seat.getSeatNo())
                        .status(seat.getStatus())
                        .build())
                .toList();

        sLog.info("[ROUTE-CACHE] Route Seat Cache Data: {}", cacheData);
        applicationEventPublisher.publishEvent(new RouteSeatGeneratedEvent(payload.routeId(), cacheData));
    }

    private List<String> generateSeatNos(VehicleProfile vehicle, VehicleTemplate template) {
        int seatCapacity = Math.toIntExact(template.getSeatCapacity());
        boolean hasFloor = template.isHasFloor();

        if (seatCapacity <= 0) {
            throw new BusinessException(ExceptionUtils.buildResultResponse(
                    INVALID_DATA_ERROR,
                    String.format("Vehicle %s has invalid seat capacity %s", vehicle.getId(), seatCapacity)
            ));
        }

        if (!hasFloor) {
            return IntStream.rangeClosed(1, seatCapacity)
                    .mapToObj(i -> String.format("%02d", i))
                    .toList();
        }

        int half = (seatCapacity + 1) / 2;
        List<String> seatNos = new ArrayList<>(seatCapacity);
        for (int i = 1; i <= Math.min(half, seatCapacity); i++) {
            seatNos.add("A" + String.format("%02d", i));
        }
        for (int i = half + 1; i <= seatCapacity; i++) {
            seatNos.add("B" + String.format("%02d", i));
        }
        return seatNos;
    }
}
