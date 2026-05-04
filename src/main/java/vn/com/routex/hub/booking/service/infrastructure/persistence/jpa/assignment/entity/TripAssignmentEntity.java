package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.assignment.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.booking.service.domain.assignment.TripAssignmentStatus;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.entity.AbstractAuditingEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
@Table(name = "TRIP_ASSIGNMENT")
public class TripAssignmentEntity extends AbstractAuditingEntity {
    @Id
    private String id;

    @Column(name = "MERCHANT_ID")
    private String merchantId;

    @Column(name = "TRIP_ID")
    private String tripId;

    @Column(name = "CREATOR")
    private String creator;

    @Column(name = "DRIVER_ID")
    private String driverId;

    @Column(name = "VEHICLE_ID")
    private String vehicleId;

    @Column(name = "TICKET_PRICE")
    private BigDecimal ticketPrice;

    @Column(name = "ASSIGNED_AT")
    private OffsetDateTime assignedAt;

    @Column(name = "UNASSIGNED_AT")
    private OffsetDateTime unAssignedAt;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private TripAssignmentStatus status;

}
