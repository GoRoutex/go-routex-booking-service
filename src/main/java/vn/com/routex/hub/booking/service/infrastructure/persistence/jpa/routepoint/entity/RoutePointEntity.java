package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.routepoint.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.entity.AbstractAuditingEntity;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ROUTE_STOP")
@SuperBuilder
public class RoutePointEntity extends AbstractAuditingEntity {

    @Id
    private String id;

    @Column(name = "ROUTE_ID")
    private String routeId;

    @Column(name = "CREATOR")
    private String creator;

    @Column(name = "STOP_ORDER")
    private String stopOrder;

    @Column(name = "PLANNED_ARRIVAL_TIME")
    private OffsetDateTime plannedArrivalTime;

    @Column(name = "PLANNED_DEPARTURE_TIME")
    private OffsetDateTime plannedDepartureTime;

    @Column(name = "ACTUAL_ARRIVAL_TIME")
    private OffsetDateTime actualArrivalTime;

    @Column(name = "ACTUAL_DEPARTURE_TIME")
    private OffsetDateTime actualDepartureTime;

    @Column(name = "NOTE")
    private String note;

    @Column(name = "OPERATION_POINT_ID")
    private String operationPointId;

    @Column(name = "STOP_NAME")
    private String stopName;

    @Column(name = "STOP_ADDRESS")
    private String stopAddress;

    @Column(name = "STOP_CITY")
    private String stopCity;

    @Column(name = "STOP_LATITUDE")
    private Double stopLatitude;

    @Column(name = "STOP_LONGITUDE")
    private Double stopLongitude;
}
