package vn.com.routex.hub.booking.service.domain.route.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.booking.service.domain.assignment.RouteAssignmentStatus;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class RouteAssignmentRecord {
    private String id;
    private String routeId;
    private String creator;
    private String merchantId;
    private String vehicleId;
    private String driverId;
    private OffsetDateTime assignedAt;
    private OffsetDateTime unAssignedAt;
    private RouteAssignmentStatus status;
    private OffsetDateTime updatedAt;
    private String updatedBy;

    public static RouteAssignmentRecord assign(
            String id,
            String routeId,
            String creator,
            String merchantId,
            String vehicleId,
            String driverId,
            OffsetDateTime assignedAt
    ) {
        return RouteAssignmentRecord.builder()
                .id(id)
                .routeId(routeId)
                .creator(creator)
                .merchantId(merchantId)
                .driverId(driverId)
                .vehicleId(vehicleId)
                .assignedAt(assignedAt)
                .status(RouteAssignmentStatus.ASSIGNED)
                .build();
    }

    public void cancel(String actor, OffsetDateTime at) {
        this.status = RouteAssignmentStatus.CANCELED;
        this.unAssignedAt = at;
        this.updatedAt = at;
        this.updatedBy = actor;
    }
}
