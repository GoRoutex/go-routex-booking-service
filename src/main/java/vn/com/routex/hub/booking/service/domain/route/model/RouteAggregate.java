package vn.com.routex.hub.booking.service.domain.route.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.booking.service.domain.auditing.AbstractAuditingEntity;
import vn.com.routex.hub.booking.service.domain.route.RouteStatus;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class RouteAggregate extends AbstractAuditingEntity {
    private String id;
    private String routeCode;
    private String creator;
    private String merchantId;
    private String pickupBranch;
    private String origin;
    private String destination;
    private OffsetDateTime plannedStartTime;
    private OffsetDateTime plannedEndTime;
    private OffsetDateTime actualStartTime;
    private OffsetDateTime actualEndTime;
    private RouteStatus status;
    private List<RouteStopPlan> stopPlans;

    public static RouteAggregate plan(
            String id,
            String routeCode,
            String creator,
            String merchantId,
            String pickupBranch,
            String origin,
            String destination,
            OffsetDateTime plannedStartTime,
            OffsetDateTime plannedEndTime,
            OffsetDateTime createdAt,
            List<RouteStopPlan> stopPlans
    ) {
        return RouteAggregate.builder()
                .id(id)
                .routeCode(routeCode)
                .creator(creator)
                .merchantId(merchantId)
                .pickupBranch(pickupBranch)
                .origin(origin)
                .destination(destination)
                .plannedStartTime(plannedStartTime)
                .plannedEndTime(plannedEndTime)
                .status(RouteStatus.PLANNED)
                .createdAt(createdAt)
                .createdBy(creator)
                .stopPlans(stopPlans == null ? new ArrayList<>() : new ArrayList<>(stopPlans))
                .build();
    }

    public void cancel(String actor, OffsetDateTime updatedAt) {
        this.status = RouteStatus.CANCELED;
        this.setCreatedBy(actor);
        this.setUpdatedAt(updatedAt);
    }
}
