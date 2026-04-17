package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.customer.entity;


import jakarta.persistence.Entity;
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
    private String customerId;
    private String membershipTierId;
    private BigDecimal currentAvailablePoints; // Available points for gift exchanging
    private BigDecimal totalPoints; // Total points for promotion evaluating.
    private OffsetDateTime promotedAt;
    private CustomerMembershipStatus status;
}
