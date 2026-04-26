package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.customer.entity;


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
import vn.com.routex.hub.booking.service.domain.customer.model.CustomerMembershipStatus;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.entity.AbstractAuditingEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "CUSTOMER_MEMBERSHIP")
public class CustomerMembershipEntity extends AbstractAuditingEntity {

    @Id
    private String id;

    @Column(name = "CUSTOMER_ID")
    private String customerId;

    @Column(name = "MEMBERSHIP_TIER_ID")
    private String membershipTierId;

    @Column(name = "CURRENT_AVAILABLE_POINTS")
    private BigDecimal currentAvailablePoints; // Available points for gift exchanging

    @Column(name = "TOTAL_POINTS")
    private BigDecimal totalPoints; // Total points for promotion evaluating.

    @Column(name = "PROMOTED_AT")
    private OffsetDateTime promotedAt;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private CustomerMembershipStatus status;
}
