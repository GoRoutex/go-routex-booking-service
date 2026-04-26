package vn.com.routex.hub.booking.service.domain.seat.port;

import vn.com.routex.hub.booking.service.domain.seat.SeatFloor;
import vn.com.routex.hub.booking.service.domain.seat.model.SeatTemplate;

import java.util.List;

public interface SeatTemplateRepositoryPort {

    List<SeatTemplate> findByVehicleTemplateIdAndFloor(String vehicleTemplateId, SeatFloor floor);

    List<SeatTemplate> findByVehicleTemplateId(String vehicleTemplateId);
}
