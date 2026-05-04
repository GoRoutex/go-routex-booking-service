package vn.com.routex.hub.booking.service.domain.route.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.booking.service.domain.assignment.TripAssignmentStatus;
import vn.com.routex.hub.booking.service.domain.auditing.AbstractAuditingEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class TripAssignmentRecord extends AbstractAuditingEntity {
    private String id;
    private String merchantId;
    private String tripId;
    private String creator;
    private String vehicleId;
    private String driverId;
    private BigDecimal ticketPrice;
    private OffsetDateTime assignedAt;
    private OffsetDateTime unAssignedAt;
    private TripAssignmentStatus status;
    public static TripAssignmentRecord assign(
            String id,
            String merchantId,
            String tripId,
            String creator,
            String vehicleId,
            String driverId,
            BigDecimal ticketPrice,
            OffsetDateTime assignedAt
    ) {
        return TripAssignmentRecord.builder()
                .id(id)
                .tripId(tripId)
                .creator(creator)
                .merchantId(merchantId)
                .driverId(driverId)
                .vehicleId(vehicleId)
                .assignedAt(assignedAt)
                .ticketPrice(ticketPrice)
                .status(TripAssignmentStatus.PENDING_ASSIGNMENT)
                .build();
    }


    public void cancel(String actor, OffsetDateTime at) {
        this.status = TripAssignmentStatus.CANCELED;
        this.unAssignedAt = at;
        this.setUpdatedAt(at);
        this.setUpdatedBy(actor);
    }
}
