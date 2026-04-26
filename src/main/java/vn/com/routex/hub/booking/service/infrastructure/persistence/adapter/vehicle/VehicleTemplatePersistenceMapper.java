package vn.com.routex.hub.booking.service.infrastructure.persistence.adapter.vehicle;


import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.domain.vehicle.model.VehicleTemplate;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.vehicle.entity.VehicleTemplateEntity;

@Component
public class VehicleTemplatePersistenceMapper {

    public VehicleTemplateEntity toEntity(VehicleTemplate vehicleTemplate) {

        if(vehicleTemplate == null) {
            return null;
        }

        return VehicleTemplateEntity
                .builder()
                .id(vehicleTemplate.getId())
                .code(vehicleTemplate.getCode())
                .name(vehicleTemplate.getName())
                .manufacturer(vehicleTemplate.getManufacturer())
                .model(vehicleTemplate.getModel())
                .seatCapacity(vehicleTemplate.getSeatCapacity())
                .category(vehicleTemplate.getCategory())
                .type(vehicleTemplate.getType())
                .fuelType(vehicleTemplate.getFuelType())
                .hasFloor(vehicleTemplate.isHasFloor())
                .ticketPrice(vehicleTemplate.getTicketPrice())
                .merchantId(vehicleTemplate.getMerchantId())
                .status(vehicleTemplate.getStatus())
                .build();
    }

    public VehicleTemplate toDomain(VehicleTemplateEntity vehicleTemplate) {
        if(vehicleTemplate == null) {
            return null;
        }
        return VehicleTemplate
                .builder()
                .id(vehicleTemplate.getId())
                .code(vehicleTemplate.getCode())
                .name(vehicleTemplate.getName())
                .manufacturer(vehicleTemplate.getManufacturer())
                .model(vehicleTemplate.getModel())
                .seatCapacity(vehicleTemplate.getSeatCapacity())
                .category(vehicleTemplate.getCategory())
                .type(vehicleTemplate.getType())
                .fuelType(vehicleTemplate.getFuelType())
                .hasFloor(vehicleTemplate.isHasFloor())
                .ticketPrice(vehicleTemplate.getTicketPrice())
                .merchantId(vehicleTemplate.getMerchantId())
                .status(vehicleTemplate.getStatus())
                .build();
    }
}
