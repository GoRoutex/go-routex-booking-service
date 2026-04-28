package vn.com.routex.hub.booking.service.infrastructure.persistence.adapter.seat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.hub.booking.service.domain.seat.model.RouteSeat;
import vn.com.routex.hub.booking.service.domain.seat.port.RouteSeatRepositoryPort;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.seat.repository.RouteSeatEntityRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RouteSeatRepositoryAdapter implements RouteSeatRepositoryPort {

    private final RouteSeatEntityRepository routeSeatEntityRepository;
    private final RouteSeatPersistenceMapper routeSeatPersistenceMapper;
    private final SystemLog sLog = SystemLog.getLogger(this.getClass());


    @Override
    public boolean existsByRouteId(String routeId) {
        return routeSeatEntityRepository.existsByRouteId(routeId);
    }

    @Override
    public List<RouteSeat> findAllByRouteIdOrderBySeatNoAsc(String routeId) {
        return routeSeatEntityRepository.findAllByRouteIdOrderBySeatNoAsc(routeId).stream()
                .map(routeSeatPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<RouteSeat> findAllByRouteIdAndSeatNoInForUpdate(String routeId, List<String> seatNos) {
        return routeSeatEntityRepository.findAllByRouteIdAndSeatNoInForUpdate(routeId, seatNos).stream()
                .map(routeSeatPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<RouteSeat> findByRouteIdAndSeatNo(String routeId, String seatNo) {
        return routeSeatEntityRepository.findByRouteIdAndSeatNo(routeId, seatNo)
                .map(routeSeatPersistenceMapper::toDomain);
    }

    @Override
    public List<RouteSeat> saveAll(List<RouteSeat> routeSeats) {
        return routeSeatEntityRepository.saveAll(routeSeats.stream()
                        .map(routeSeatPersistenceMapper::toEntity)
                        .toList()).stream()
                .map(routeSeatPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public RouteSeat save(RouteSeat routeSeat) {
        return routeSeatPersistenceMapper.toDomain(
                routeSeatEntityRepository.save(routeSeatPersistenceMapper.toEntity(routeSeat))
        );
    }
}
