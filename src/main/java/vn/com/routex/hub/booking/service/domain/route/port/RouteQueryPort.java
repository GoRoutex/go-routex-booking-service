package vn.com.routex.hub.booking.service.domain.route.port;



import vn.com.routex.hub.booking.service.domain.common.PagedResult;
import vn.com.routex.hub.booking.service.domain.route.readmodel.RouteFetchView;
import vn.com.routex.hub.booking.service.domain.route.readmodel.RouteSearchView;

import java.time.OffsetDateTime;
import java.util.List;

public interface RouteQueryPort {
    List<RouteSearchView> searchAssignedRoutes(
            String merchantId,
            String origin,
            String destination,
            OffsetDateTime startTime,
            OffsetDateTime endTime,
            int pageNumber,
            int pageSize
    );

    PagedResult<RouteFetchView> fetchRoutes(String merchantId, String merchantName, int pageNumber, int pageSize);

}
