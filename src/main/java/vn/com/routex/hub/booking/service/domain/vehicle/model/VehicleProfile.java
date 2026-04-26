package vn.com.routex.hub.booking.service.domain.vehicle.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.booking.service.domain.auditing.AbstractAuditingEntity;
import vn.com.routex.hub.booking.service.domain.vehicle.VehicleStatus;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class VehicleProfile extends AbstractAuditingEntity {
    private String id;
    private String merchantId;
    private String templateId;
    private String creator;
    private VehicleStatus status;
    private String vehiclePlate;

    public static VehicleProfile register(
            String id,
            String merchantId,
            String templateId,
            String creator,
            String vehiclePlate,
            OffsetDateTime createdAt
    ) {
        return VehicleProfile.builder()
                .id(id)
                .merchantId(merchantId)
                .templateId(templateId)
                .creator(creator)
                .status(VehicleStatus.AVAILABLE)
                .vehiclePlate(vehiclePlate)
                .createdAt(createdAt)
                .createdBy(creator)
                .build();
    }
}
