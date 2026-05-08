package vn.com.routex.hub.booking.service.domain.department.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.booking.service.domain.auditing.AbstractAuditingEntity;
import vn.com.routex.hub.booking.service.domain.department.DepartmentStatus;
import vn.com.routex.hub.booking.service.domain.department.DepartmentType;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Department extends AbstractAuditingEntity {
    private String id;
    private String name;
    private String merchantId;
    private DepartmentType type;
    private String address;
    private String wardId;
    private String wardName;
    private String provinceId;
    private String provinceName;
    private String note;
    private String openingTime;
    private String closingTime;
    private String onlineOpeningTime;
    private String onlineClosingTime;
    private Double latitude;
    private Double longitude;
    private DepartmentStatus status;
}
