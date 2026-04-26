package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.seat.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.booking.service.domain.seat.SeatFloor;
import vn.com.routex.hub.booking.service.domain.seat.model.SeatTemplate;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.seat.entity.SeatTemplateEntity;

import java.util.Collection;
import java.util.List;

@Repository
public interface SeatTemplateEntityRepository extends JpaRepository<SeatTemplateEntity, String> {
    List<SeatTemplateEntity> findByVehicleTemplateId(String vehicleTemplateId);

    List<SeatTemplateEntity> findByVehicleTemplateIdAndFloor(String vehicleTemplateId, SeatFloor floor);
}
