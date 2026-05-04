package vn.com.routex.hub.booking.service.infrastructure.persistence.adapter.route;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.domain.assignment.TripAssignmentStatus;
import vn.com.routex.hub.booking.service.domain.route.model.TripAssignmentRecord;
import vn.com.routex.hub.booking.service.domain.route.port.TripAssignmentRepositoryPort;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.assignment.entity.TripAssignmentEntity;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.assignment.repository.TripAssignmentEntityRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TripAssignmentRepositoryAdapter implements TripAssignmentRepositoryPort {

    private final TripAssignmentEntityRepository tripAssignmentEntityRepository;
    private final TripAssignmentPersistenceMapper tripAssignmentPersistenceMapper;

    @Override
    public boolean existsActiveByTripId(String tripId) {
        return findActiveByTripId(tripId).isPresent();
    }

    @Override
    public Optional<TripAssignmentRecord> findByTripId(String tripId) {
        return tripAssignmentEntityRepository.findByTripId(tripId)
                .map(tripAssignmentPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsActiveByTripId(String tripId, String merchantId) {
        return findActiveByTripId(tripId, merchantId).isPresent();
    }

    @Override
    public Optional<TripAssignmentRecord> findActiveByTripId(String tripId) {
        return tripAssignmentEntityRepository
                .findFirstByTripIdAndStatusAndUnAssignedAtIsNullOrderByAssignedAtDesc(tripId, TripAssignmentStatus.ASSIGNED)
                .map(tripAssignmentPersistenceMapper::toDomain);
    }

    @Override
    public Optional<TripAssignmentRecord> findActiveByTripId(String tripId, String merchantId) {
        return tripAssignmentEntityRepository
                .findFirstByTripIdAndMerchantIdAndStatusAndUnAssignedAtIsNullOrderByAssignedAtDesc(
                        tripId,
                        merchantId,
                        TripAssignmentStatus.ASSIGNED
                )
                .map(tripAssignmentPersistenceMapper::toDomain);
    }

    @Override
    public Map<String, TripAssignmentRecord> findLatestActiveByTripIds(List<String> tripIds) {
        return toLatestAssignmentMap(
                tripAssignmentEntityRepository.findActiveByTripIdsNative(tripIds, TripAssignmentStatus.ASSIGNED.name())
        );
    }

    @Override
    public Map<String, TripAssignmentRecord> findLatestActiveByTripIds(List<String> tripIds, String merchantId) {
        return toLatestAssignmentMap(
                tripAssignmentEntityRepository.findActiveByTripIdsAndMerchantIdNative(
                        tripIds,
                        merchantId,
                        TripAssignmentStatus.ASSIGNED.name()
                )
        );
    }

    @Override
    public List<TripAssignmentRecord> findByMerchantId(String merchantId) {
        return tripAssignmentEntityRepository.findByMerchantId(merchantId).stream()
                .map(tripAssignmentPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public void save(TripAssignmentRecord assignment) {
        tripAssignmentEntityRepository.save(tripAssignmentPersistenceMapper.toJpaEntity(assignment));
    }

    private Map<String, TripAssignmentRecord> toLatestAssignmentMap(List<TripAssignmentEntity> entities) {
        Map<String, TripAssignmentRecord> result = new LinkedHashMap<>();
        for (TripAssignmentEntity entity : entities) {
            TripAssignmentRecord record = tripAssignmentPersistenceMapper.toDomain(entity);
            result.putIfAbsent(record.getTripId(), record);
        }
        return result;
    }
}
