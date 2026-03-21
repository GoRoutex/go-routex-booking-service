package vn.com.routex.hub.booking.service.infrastructure.persistence.adapter.seat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.domain.seat.model.RouteSeat;
import vn.com.routex.hub.booking.service.domain.seat.port.RouteSeatRepositoryPort;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.seat.repository.RouteSeatJpaRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RouteSeatRepositoryAdapter implements RouteSeatRepositoryPort {

    private final RouteSeatJpaRepository routeSeatJpaRepository;
    private final RouteSeatPersistenceMapper routeSeatPersistenceMapper;

    @Override
    public boolean existsByRouteId(String routeId) {
        return routeSeatJpaRepository.existsByRouteId(routeId);
    }

    @Override
    public List<RouteSeat> findAllByRouteIdOrderBySeatNoAsc(String routeId) {
        return routeSeatJpaRepository.findAllByRouteIdOrderBySeatNoAsc(routeId).stream()
                .map(routeSeatPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<RouteSeat> findAllByRouteIdAndSeatNoInForUpdate(String routeId, List<String> seatNos) {
        return routeSeatJpaRepository.findAllByRouteIdAndSeatNoInForUpdate(routeId, seatNos).stream()
                .map(routeSeatPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<RouteSeat> findByRouteIdAndSeatNo(String routeId, String seatNo) {
        return routeSeatJpaRepository.findByRouteIdAndSeatNo(routeId, seatNo)
                .map(routeSeatPersistenceMapper::toDomain);
    }

    @Override
    public List<RouteSeat> saveAll(List<RouteSeat> routeSeats) {
        return routeSeatJpaRepository.saveAll(routeSeats.stream()
                        .map(routeSeatPersistenceMapper::toJpaEntity)
                        .toList()).stream()
                .map(routeSeatPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public RouteSeat save(RouteSeat routeSeat) {
        return routeSeatPersistenceMapper.toDomain(
                routeSeatJpaRepository.save(routeSeatPersistenceMapper.toJpaEntity(routeSeat))
        );
    }
}
