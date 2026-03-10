package vn.com.routex.hub.booking.service.domain.stoppoint;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.booking.service.domain.auditing.AbstractAuditingEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "STOP_POINT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class StopPoint extends AbstractAuditingEntity {
    @Id
    private String id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "LATITUDE")
    private BigDecimal latitude; // Vi Do

    @Column(name = "LONGTITUDE")
    private BigDecimal longtitude; // Kinh do

    @Column(name = "TYPE")
    private StopType type;
}
