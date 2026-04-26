package vn.com.routex.hub.booking.service.infrastructure.persistence.adapter.seat;


import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.domain.seat.model.SeatTemplate;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.seat.entity.SeatTemplateEntity;

@Component
public class SeatTemplatePersistenceMapper {

    public SeatTemplate toDomain(SeatTemplateEntity entity) {
        if(entity == null) {
            return null;
        }

        return SeatTemplate.builder()
                .id(entity.getId())
                .vehicleTemplateId(entity.getVehicleTemplateId())
                .seatCode(entity.getSeatCode())
                .floor(entity.getFloor())
                .rowNo(entity.getRowNo())
                .columnNo(entity.getColumnNo())
                .type(entity.getType())
                .isActive(entity.isActive())
                .build();
    }

    public SeatTemplateEntity toEntity(SeatTemplate domain) {
        if(domain == null) {
            return null;
        }

        return SeatTemplateEntity.builder()
                .id(domain.getId())
                .vehicleTemplateId(domain.getVehicleTemplateId())
                .seatCode(domain.getSeatCode())
                .floor(domain.getFloor())
                .rowNo(domain.getRowNo())
                .columnNo(domain.getColumnNo())
                .type(domain.getType())
                .isActive(domain.isActive())
                .build();
    }
}
