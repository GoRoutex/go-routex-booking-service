package vn.com.routex.hub.booking.service.domain.seat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.booking.service.domain.auditing.AbstractAuditingEntity;
import vn.com.routex.hub.booking.service.domain.seat.SeatFloor;
import vn.com.routex.hub.booking.service.domain.seat.SeatType;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SeatTemplate extends AbstractAuditingEntity {
    private String id;
    private String vehicleTemplateId;
    private String seatCode;
    private SeatFloor floor;
    private int rowNo;
    private int columnNo;
    private SeatType type;
    private boolean isActive;
}
