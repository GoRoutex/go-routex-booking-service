package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.customer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.booking.service.domain.customer.CustomerStatus;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.entity.AuditingEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "CUSTOMER")
public class CustomerEntity extends AuditingEntity {

    @Id
    private String id;

    @Column(name = "USER_ID", nullable = false)
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private CustomerStatus status;

    @Column(name = "FULL_NAME")
    private String fullName;

    @Column(name = "TOTAL_TRIPS")
    @Builder.Default
    private Integer totalTrips = 0;

    @Column(name = "TRIP_POINTS")
    @Builder.Default
    private BigDecimal tripPoints = BigDecimal.ZERO;

    @Column(name = "TOTAL_SPENT")
    @Builder.Default
    private BigDecimal totalSpent = BigDecimal.ZERO;

    private OffsetDateTime lastBookingAt;

    private OffsetDateTime lastTripAt;
}
