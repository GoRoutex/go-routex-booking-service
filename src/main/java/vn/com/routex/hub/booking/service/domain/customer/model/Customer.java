package vn.com.routex.hub.booking.service.domain.customer.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.booking.service.domain.auditing.AbstractAuditingEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Customer extends AbstractAuditingEntity {

    private String id;
    private String userId;
    private String fullName;
    private CustomerStatus status;
    @Builder.Default
    private Integer totalTrips = 0;
    @Builder.Default
    private BigDecimal tripPoints = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal totalSpent = BigDecimal.ZERO;
    private OffsetDateTime lastBookingAt;
    private OffsetDateTime lastTripAt;
}
