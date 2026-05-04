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
import vn.com.routex.hub.booking.service.domain.seat.SeatStatus;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.entity.AbstractAuditingEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "TRIP_SEAT")
public class TripSeatEntity extends AbstractAuditingEntity {

    @Id
    private String id;

    @Column(name = "TRIP_ID")
    private String tripId;

    @Column(name = "SEAT_NO")
    private String seatNo;

    @Column(name = "SEAT_TEMPLATE_ID")
    private String seatTemplateId;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    @Column(name = "CREATOR")
    private String creator;
}
