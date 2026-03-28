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
import vn.com.routex.hub.booking.service.domain.assignment.RouteAssignmentStatus;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.entity.AuditingEntity;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "ROUTE_ASSIGNMENT")
public class RouteAssignmentEntity extends AuditingEntity {

    @Id
    private String id;

    @Column(name = "ROUTE_ID")
    private String routeId;

    @Column(name = "CREATOR")
    private String creator;

    @Column(name = "VEHICLE_ID")
    private String vehicleId;

    @Column(name = "ASSIGNED_AT")
    private OffsetDateTime assignedAt;

    @Column(name = "UNASSIGNED_AT")
    private OffsetDateTime unAssignedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private RouteAssignmentStatus status;
}
