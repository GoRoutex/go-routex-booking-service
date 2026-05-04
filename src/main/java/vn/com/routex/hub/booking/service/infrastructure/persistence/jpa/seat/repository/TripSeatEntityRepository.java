package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.seat.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.seat.entity.TripSeatEntity;

import java.util.List;
import java.util.Optional;


@Repository
public interface TripSeatEntityRepository extends JpaRepository<TripSeatEntity, String> {

    boolean existsByTripId(String tripId);

    List<TripSeatEntity> findAllByTripIdOrderBySeatNoAsc(String tripId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = """
                   select rs
                   from TripSeatEntity rs
                   where rs.tripId = :tripId
                   and rs.seatNo in :seatNos
                   """)
    List<TripSeatEntity> findAllByTripIdAndSeatNoInForUpdate(@Param("tripId") String tripId,
                                                              @Param("seatNos") List<String> seatNos);

    Optional<TripSeatEntity> findByTripIdAndSeatNo(String routeId, String seatNo);
}
