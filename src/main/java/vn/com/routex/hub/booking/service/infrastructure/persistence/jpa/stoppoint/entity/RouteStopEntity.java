package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.stoppoint.entity;

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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ROUTE_STOP")
@SuperBuilder
public class RouteStopEntity extends AbstractAuditingEntity {

    @Id
    private String id;

    @Column(name = "ROUTE_ID")
    private String routeId;

    @Column(name = "CREATOR")
    private String creator;

    @Column(name = "STOP_ORDER")
    private String stopOrder;

    @Column(name = "NOTE")
    private String note;

    @Column(name = "DEPARTMENT_ID")
    private String departmentId;

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

    @Column(name = "STAY_DURATION")
    private Long stayDuration;

    @Column(name = "TIME_AT_DEPARTMENT")
    private Integer timeAtDepartment;
}
