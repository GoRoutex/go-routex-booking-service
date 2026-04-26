package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.seat.entity;


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
import vn.com.routex.hub.booking.service.domain.seat.SeatFloor;
import vn.com.routex.hub.booking.service.domain.seat.SeatType;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.entity.AbstractAuditingEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "SEAT_TEMPLATE")
public class SeatTemplateEntity extends AbstractAuditingEntity {

    @Id
    private String id;

    @Column(name = "VEHICLE_TEMPLATE_ID", nullable = false)
    private String vehicleTemplateId;

    @Column(name = "SEAT_CODE")
    private String seatCode;

    @Column(name = "FLOOR")
    @Enumerated(EnumType.STRING)
    private SeatFloor floor;

    @Column(name = "ROW_NO")
    private int rowNo;

    @Column(name = "COLUMN_NO")
    private int columnNo;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private SeatType type;

    @Column(name = "IS_ACTIVE")
    private boolean isActive;

}
