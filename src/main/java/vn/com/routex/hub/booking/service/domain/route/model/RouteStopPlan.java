package vn.com.routex.hub.booking.service.domain.route.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class RouteStopPlan {
    private String id;
    private String routeId;
    private String creator;
    private int stopOrder;
    private OffsetDateTime plannedArrivalTime;
    private OffsetDateTime plannedDepartureTime;
    private String note;
    private String operationPointId;
    private String stopName;
    private String stopAddress;
    private String stopCity;
    private Double stopLatitude;
    private Double stopLongitude;
    private OffsetDateTime createdAt;
    private String createdBy;
}
