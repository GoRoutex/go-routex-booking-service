package vn.com.routex.hub.booking.service.domain.route.port;



import vn.com.routex.hub.booking.service.domain.route.model.RouteAssignmentRecord;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RouteAssignmentRepositoryPort {
    boolean existsActiveByRouteId(String routeId);


    Optional<RouteAssignmentRecord> findByRouteId(String routeId);

    boolean existsActiveByRouteId(String routeId, String merchantId);

    Optional<RouteAssignmentRecord> findActiveByRouteId(String routeId);

    Optional<RouteAssignmentRecord> findActiveByRouteId(String routeId, String merchantId);

    Map<String, RouteAssignmentRecord> findLatestActiveByRouteIds(List<String> routeIds);

    Map<String, RouteAssignmentRecord> findLatestActiveByRouteIds(List<String> routeIds, String merchantId);

    List<RouteAssignmentRecord> findByMerchantId(String merchantId);

    void save(RouteAssignmentRecord assignment);
}
