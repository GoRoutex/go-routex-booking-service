package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.vehicle.entity;

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
import vn.com.routex.hub.booking.service.domain.vehicle.VehicleStatus;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.entity.AbstractAuditingEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
@Table(name = "VEHICLE")
public class VehicleEntity extends AbstractAuditingEntity {

    @Id
    private String id;

    @Column(name = "MERCHANT_ID")
    private String merchantId;

    @Column(name = "TEMPLATE_ID")
    private String templateId;

    @Column(name = "CREATOR")
    private String creator;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private VehicleStatus status;

    @Column(name = "VEHICLE_PLATE", nullable = false)
    private String vehiclePlate;
}