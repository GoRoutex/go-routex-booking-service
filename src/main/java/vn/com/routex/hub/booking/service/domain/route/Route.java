package vn.com.routex.hub.booking.service.domain.route;


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
import vn.com.routex.hub.booking.service.domain.auditing.AbstractAuditingEntity;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
@Table(name = "ROUTE")
public class Route extends AbstractAuditingEntity {

    @Id
    private String id;
    @Column(name = "ROUTE_CODE")
    private String routeCode;

    @Column(name = "CREATOR")
    private String creator;

    @Column(name = "PICKUP_BRANCH")
    private String pickupBranch;

    @Column(name = "ORIGIN")
    private String origin;

    @Column(name = "DESTINATION")
    private String destination;

    @Column(name = "PLANNED_START_TIME")
    private OffsetDateTime plannedStartTime;

    @Column(name = "PLANNED_END_TIME")
    private OffsetDateTime plannedEndTime;

    @Column(name = "ACTUAL_START_TIME")
    private OffsetDateTime actualStartTime;

    @Column(name = "ACTUAL_END_TIME")
    private OffsetDateTime actualEndTime;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private RouteStatus status;
}
