package vn.com.routex.hub.booking.service.infrastructure.persistence.adapter.seat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.hub.booking.service.domain.seat.model.TripSeat;
import vn.com.routex.hub.booking.service.domain.seat.port.TripSeatRepositoryPort;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.seat.repository.TripSeatEntityRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TripSeatRepositoryAdapter implements TripSeatRepositoryPort {

    private final TripSeatEntityRepository tripSeatEntityRepository;
    private final TripSeatPersistenceMapper tripSeatPersistenceMapper;
    private final SystemLog sLog = SystemLog.getLogger(this.getClass());


    @Override
    public boolean existsByTripId(String tripId) {
        return tripSeatEntityRepository.existsByTripId(tripId);
    }

    @Override
    public List<TripSeat> findAllByTripIdOrderBySeatNoAsc(String tripId) {
        return tripSeatEntityRepository.findAllByTripIdOrderBySeatNoAsc(tripId).stream()
                .map(tripSeatPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<TripSeat> findAllByTripIdAndSeatNoInForUpdate(String tripId, List<String> seatNos) {
        return tripSeatEntityRepository.findAllByTripIdAndSeatNoInForUpdate(tripId, seatNos).stream()
                .map(tripSeatPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<TripSeat> findByTripIdAndSeatNo(String tripId, String seatNo) {
        return tripSeatEntityRepository.findByTripIdAndSeatNo(tripId, seatNo)
                .map(tripSeatPersistenceMapper::toDomain);
    }

    @Override
    public List<TripSeat> saveAll(List<TripSeat> tripSeats) {
        return tripSeatEntityRepository.saveAll(tripSeats.stream()
                        .map(tripSeatPersistenceMapper::toEntity)
                        .toList()).stream()
                .map(tripSeatPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public TripSeat save(TripSeat tripSeat) {
        return tripSeatPersistenceMapper.toDomain(
                tripSeatEntityRepository.save(tripSeatPersistenceMapper.toEntity(tripSeat))
        );
    }
}
