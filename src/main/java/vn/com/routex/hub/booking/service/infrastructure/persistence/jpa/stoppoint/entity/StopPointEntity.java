package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.stoppoint.entity;

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
import vn.com.routex.hub.booking.service.domain.stoppoint.StopType;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.entity.AuditingEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "STOP_POINT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class StopPointEntity extends AuditingEntity {

    @Id
    private String id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "LATITUDE")
    private BigDecimal latitude;

    @Column(name = "LONGTITUDE")
    private BigDecimal longtitude;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    private StopType type;
}
