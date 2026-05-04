package vn.com.routex.hub.booking.service.application.handler.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.hub.booking.service.application.handler.TripEvent;
import vn.com.routex.hub.booking.service.domain.seat.SeatFloor;
import vn.com.routex.hub.booking.service.domain.seat.SeatStatus;
import vn.com.routex.hub.booking.service.domain.seat.model.SeatTemplate;
import vn.com.routex.hub.booking.service.domain.seat.model.TripSeat;
import vn.com.routex.hub.booking.service.domain.seat.port.SeatTemplateRepositoryPort;
import vn.com.routex.hub.booking.service.domain.seat.port.TripSeatRepositoryPort;
import vn.com.routex.hub.booking.service.domain.vehicle.model.VehicleProfile;
import vn.com.routex.hub.booking.service.domain.vehicle.model.VehicleTemplate;
import vn.com.routex.hub.booking.service.domain.vehicle.port.VehicleRepositoryPort;
import vn.com.routex.hub.booking.service.domain.vehicle.port.VehicleTemplateRepositoryPort;
import vn.com.routex.hub.booking.service.infrastructure.cache.redis.models.TripCacheSeat;
import vn.com.routex.hub.booking.service.infrastructure.kafka.event.DomainEvent;
import vn.com.routex.hub.booking.service.infrastructure.kafka.event.TripSeatGeneratedEvent;
import vn.com.routex.hub.booking.service.infrastructure.kafka.event.TripSellableEvent;
import vn.com.routex.hub.booking.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.booking.service.infrastructure.persistence.utils.ExceptionUtils;
import vn.com.routex.hub.booking.service.interfaces.models.base.BaseRequest;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.DUPLICATE_ERROR;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.INVALID_DATA_ERROR;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.ROUTE_SEAT_EXIST;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.SEAT_TEMPLATE_NOT_FOUND;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.VEHICLE_NOT_FOUND;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.VEHICLE_TEMPLATE_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class TripEventHandler implements TripEvent {

    private final VehicleRepositoryPort vehicleRepositoryPort;
    private final TripSeatRepositoryPort tripSeatRepositoryPort;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final VehicleTemplateRepositoryPort vehicleTemplateRepositoryPort;
    private final SeatTemplateRepositoryPort seatTemplateRepositoryPort;

    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @Override
    @Transactional
    public void generateRouteSeat(DomainEvent event, BaseRequest context, TripSellableEvent payload) {
        VehicleProfile vehicle = vehicleRepositoryPort.findById(payload.vehicleId())
                .orElseThrow(() -> new BusinessException(context.getRequestId(), context.getRequestDateTime(), context.getChannel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, VEHICLE_NOT_FOUND)));

        VehicleTemplate template = vehicleTemplateRepositoryPort.findById(vehicle.getTemplateId())
                .orElseThrow(() -> new BusinessException(ExceptionUtils.buildResultResponse(VEHICLE_NOT_FOUND, VEHICLE_TEMPLATE_NOT_FOUND)));

        if (tripSeatRepositoryPort.existsByTripId(payload.tripId())) {
            throw new BusinessException(context.getRequestId(), context.getRequestDateTime(), context.getChannel(),
                    ExceptionUtils.buildResultResponse(DUPLICATE_ERROR, String.format(ROUTE_SEAT_EXIST, payload.tripId())));
        }

        sLog.info("[TRIP-SEAT] Generate seats tripId={} vehicleId={} seatCapacity={} hasFloor={}",
                payload.tripId(), payload.vehicleId(), template.getSeatCapacity(), template.isHasFloor());

        List<SeatTemplate> seatTemplates = generateSeatNos(vehicle, template);
        Map<String, SeatTemplate> templateMap = seatTemplates.stream()
                .collect(Collectors.toMap(SeatTemplate::getId, Function.identity()));

        List<TripSeat> seats = seatTemplates.stream()
                .map(seatTemplate -> TripSeat.builder()
                        .id(UUID.randomUUID().toString())
                        .tripId(payload.tripId())
                        .seatNo(seatTemplate.getSeatCode())
                        .status(SeatStatus.AVAILABLE)
                        .seatTemplateId(seatTemplate.getId())
                        .creator(payload.creator())
                        .createdAt(OffsetDateTime.now())
                        .createdBy(payload.creator())
                        .build())
                .collect(Collectors.toList());

        List<TripSeat> savedSeats = tripSeatRepositoryPort.saveAll(seats);

        List<TripCacheSeat> cacheData = savedSeats.stream()
                .map(seat -> {
                    SeatTemplate seatTemplate = templateMap.get(seat.getSeatTemplateId());
                    return TripCacheSeat.builder()
                            .tripId(seat.getTripId())
                            .seatId(seat.getId())
                            .seatNo(seat.getSeatNo())
                            .status(seat.getStatus())
                            .floor(seatTemplate.getFloor())
                            .rowNo(seatTemplate.getRowNo())
                            .colNo(seatTemplate.getColumnNo())
                            .build();
                })
                .toList();

        sLog.info("[ROUTE-CACHE] Route Seat Cache Data: {}", cacheData);
        applicationEventPublisher.publishEvent(new TripSeatGeneratedEvent(payload.tripId(), cacheData));
    }

    private List<SeatTemplate> generateSeatNos(VehicleProfile vehicle, VehicleTemplate template) {

        int seatCapacity = Math.toIntExact(template.getSeatCapacity());
        boolean hasFloor = template.isHasFloor();

        if (seatCapacity <= 0) {
            throw new BusinessException(ExceptionUtils.buildResultResponse(
                    INVALID_DATA_ERROR,
                    String.format("Vehicle %s has invalid seat capacity %s", vehicle.getId(), seatCapacity)
            ));
        }

        List<SeatTemplate> seatTemplateList;

        if (!hasFloor) {
            seatTemplateList = seatTemplateRepositoryPort.findByVehicleTemplateIdAndFloor(template.getId(), SeatFloor.NONE);
        } else {
            seatTemplateList = seatTemplateRepositoryPort.findByVehicleTemplateId(template.getId());
        }

        if (seatTemplateList.isEmpty()) {
            throw new BusinessException(ExceptionUtils.buildResultResponse(
                    RECORD_NOT_FOUND, String.format(SEAT_TEMPLATE_NOT_FOUND, template.getId())));
        }

        sLog.info("Seat Template List: {}", seatTemplateList);

        return seatTemplateList;
    }
}
