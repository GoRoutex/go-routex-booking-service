package vn.com.routex.hub.booking.service.application.handler.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.hub.booking.service.application.handler.RouteEvent;
import vn.com.routex.hub.booking.service.domain.seat.RouteSeat;
import vn.com.routex.hub.booking.service.domain.seat.RouteSeatRepository;
import vn.com.routex.hub.booking.service.domain.seat.SeatStatus;
import vn.com.routex.hub.booking.service.domain.vehicle.Vehicle;
import vn.com.routex.hub.booking.service.domain.vehicle.VehicleRepository;
import vn.com.routex.hub.booking.service.infrastructure.kafka.event.RouteSellableEvent;
import vn.com.routex.hub.booking.service.infrastructure.kafka.model.KafkaEventMessage;
import vn.com.routex.hub.booking.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.booking.service.infrastructure.persistence.utils.ExceptionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.DUPLICATE_ERROR;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.ROUTE_SEAT_EXIST;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.VEHICLE_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class RouteEventHandler implements RouteEvent {

    private final VehicleRepository vehicleRepository;
    private final RouteSeatRepository routeSeatRepository;

    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @Override
    @Transactional
    public void generateRouteSeat(KafkaEventMessage<RouteSellableEvent> payload) {
        Vehicle vehicle = vehicleRepository.findById(payload.data().vehicleId())
                .orElseThrow(() -> new BusinessException(payload.requestId(), payload.requestDateTime(), payload.channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, VEHICLE_NOT_FOUND)));

        if(routeSeatRepository.existsByRouteId(payload.data().routeId())) {
            throw new BusinessException(payload.requestId(), payload.requestDateTime(), payload.channel(),
                    ExceptionUtils.buildResultResponse(DUPLICATE_ERROR, String.format(ROUTE_SEAT_EXIST, payload.data().routeId())));
        }

        List<String> seatNos = generateSeatNos(vehicle);
        sLog.info("[ROUTE-SEAT] RouteId {} Seat List {}", payload.data().routeId(), seatNos);
        List<RouteSeat> seats = seatNos.stream()
                .map(seatNo -> RouteSeat.builder()
                        .routeId(payload.data().routeId())
                        .seatNo(seatNo)
                        .status(SeatStatus.AVAILABLE)
                        .creator(payload.data().creator())
                        .build())
                .collect(Collectors.toList());
        routeSeatRepository.saveAll(seats);
    }

    private List<String> generateSeatNos(Vehicle vehicle) {
        int seatCapacity = vehicle.getSeatCapacity();
        boolean hasFloor = vehicle.isHasFloor();

        if(seatCapacity <= 0) return List.of();

        if(!hasFloor) {
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

        sLog.info("SeatNos: {}", seatNos);
        return seatNos;
    }
}
