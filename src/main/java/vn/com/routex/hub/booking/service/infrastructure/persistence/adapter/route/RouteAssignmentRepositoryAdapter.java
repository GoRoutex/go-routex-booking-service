package vn.com.routex.hub.booking.service.infrastructure.persistence.adapter.route;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.domain.assignment.RouteAssignmentStatus;
import vn.com.routex.hub.booking.service.domain.route.model.RouteAssignmentRecord;
import vn.com.routex.hub.booking.service.domain.route.port.RouteAssignmentRepositoryPort;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.assignment.entity.RouteAssignmentEntity;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.assignment.repository.RouteAssignmentEntityRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RouteAssignmentRepositoryAdapter implements RouteAssignmentRepositoryPort {

    private final RouteAssignmentEntityRepository routeAssignmentEntityRepository;
    private final RouteAssignmentPersistenceMapper routeAssignmentPersistenceMapper;

    @Override
    public boolean existsActiveByRouteId(String routeId) {
        return findActiveByRouteId(routeId).isPresent();
    }

    @Override
    public boolean existsActiveByRouteId(String routeId, String merchantId) {
        return findActiveByRouteId(routeId, merchantId).isPresent();
    }

    @Override
    public java.util.Optional<RouteAssignmentRecord> findActiveByRouteId(String routeId) {
        return routeAssignmentEntityRepository
                .findFirstByRouteIdAndStatusAndUnAssignedAtIsNullOrderByAssignedAtDesc(routeId, RouteAssignmentStatus.ASSIGNED)
                .map(routeAssignmentPersistenceMapper::toDomain);
    }

    @Override
    public java.util.Optional<RouteAssignmentRecord> findActiveByRouteId(String routeId, String merchantId) {
        return routeAssignmentEntityRepository
                .findFirstByRouteIdAndMerchantIdAndStatusAndUnAssignedAtIsNullOrderByAssignedAtDesc(
                        routeId,
                        merchantId,
                        RouteAssignmentStatus.ASSIGNED
                )
                .map(routeAssignmentPersistenceMapper::toDomain);
    }

    @Override
    public Map<String, RouteAssignmentRecord> findLatestActiveByRouteIds(List<String> routeIds) {
        return toLatestAssignmentMap(
                routeAssignmentEntityRepository.findActiveByRouteIdsNative(routeIds, RouteAssignmentStatus.ASSIGNED.name())
        );
    }

    @Override
    public Map<String, RouteAssignmentRecord> findLatestActiveByRouteIds(List<String> routeIds, String merchantId) {
        return toLatestAssignmentMap(
                routeAssignmentEntityRepository.findActiveByRouteIdsAndMerchantIdNative(
                        routeIds,
                        merchantId,
                        RouteAssignmentStatus.ASSIGNED.name()
                )
        );
    }

    @Override
    public List<RouteAssignmentRecord> findByMerchantId(String merchantId) {
        return routeAssignmentEntityRepository.findByMerchantId(merchantId).stream()
                .map(routeAssignmentPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public void save(RouteAssignmentRecord assignment) {
        routeAssignmentEntityRepository.save(routeAssignmentPersistenceMapper.toJpaEntity(assignment));
    }

    private Map<String, RouteAssignmentRecord> toLatestAssignmentMap(List<RouteAssignmentEntity> entities) {
        Map<String, RouteAssignmentRecord> result = new LinkedHashMap<>();
        for (RouteAssignmentEntity entity : entities) {
            RouteAssignmentRecord record = routeAssignmentPersistenceMapper.toDomain(entity);
            result.putIfAbsent(record.getRouteId(), record);
        }
        return result;
    }
}
