package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.department.entity;


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
import vn.com.routex.hub.booking.service.domain.department.DepartmentStatus;
import vn.com.routex.hub.booking.service.domain.department.DepartmentType;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.entity.AbstractAuditingEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "DEPARTMENT")
public class DepartmentEntity extends AbstractAuditingEntity {
    @Id
    private String id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "MERCHANT_ID")
    private String merchantId;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private DepartmentType type;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "WARD_ID")
    private String wardId;

    @Column(name = "WARD_NAME")
    private String wardName;

    @Column(name = "PROVINCE_ID")
    private String provinceId;

    @Column(name = "PROVINCE_NAME")
    private String provinceName;

    @Column(name = "NOTE")
    private String note;

    @Column(name = "OPENING_TIME")
    private String openingTime;

    @Column(name = "CLOSING_TIME")
    private String closingTime;

    @Column(name = "ONLINE_OPENING_TIME")
    private String onlineOpeningTime;

    @Column(name = "ONLINE_CLOSING_TIME")
    private String onlineClosingTime;

    @Column(name = "LATITUDE")
    private Double latitude;

    @Column(name = "LONGITUDE")
    private Double longitude;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private DepartmentStatus status;
}
