package vn.com.routex.hub.booking.service.domain.membership.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.booking.service.domain.auditing.AbstractAuditingEntity;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class MembershipTier extends AbstractAuditingEntity {
    private String id;
    private MembershipBadge badge;
    private BigDecimal minPoints;
    private BigDecimal pointMultiplier;
    private Integer priorityLevel;
    private Integer discountPercent;
}
