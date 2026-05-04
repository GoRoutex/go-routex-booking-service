package vn.com.routex.hub.booking.service.domain.seat.port;

import vn.com.routex.hub.booking.service.domain.seat.model.TripSeat;

import java.util.List;
import java.util.Optional;

public interface TripSeatRepositoryPort {
    boolean existsByTripId(String routeId);

    List<TripSeat> findAllByTripIdOrderBySeatNoAsc(String tripId);

    List<TripSeat> findAllByTripIdAndSeatNoInForUpdate(String tripId, List<String> seatNos);

    Optional<TripSeat> findByTripIdAndSeatNo(String tripId, String seatNo);

    List<TripSeat> saveAll(List<TripSeat> tripSeats);

    TripSeat save(TripSeat tripSeat);
}
