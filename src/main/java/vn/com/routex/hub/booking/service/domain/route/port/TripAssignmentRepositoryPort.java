package vn.com.routex.hub.booking.service.domain.route.port;



import vn.com.routex.hub.booking.service.domain.route.model.TripAssignmentRecord;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TripAssignmentRepositoryPort {
    boolean existsActiveByTripId(String tripId);

    Optional<TripAssignmentRecord> findByTripId(String tripId);

    boolean existsActiveByTripId(String tripId, String merchantId);

    Optional<TripAssignmentRecord> findActiveByTripId(String tripId);

    Optional<TripAssignmentRecord> findActiveByTripId(String tripId, String merchantId);

    Map<String, TripAssignmentRecord> findLatestActiveByTripIds(List<String> tripIds);

    Map<String, TripAssignmentRecord> findLatestActiveByTripIds(List<String> tripIds, String merchantId);

    List<TripAssignmentRecord> findByMerchantId(String merchantId);

    void save(TripAssignmentRecord assignment);
}
