package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.customer.repository;

import java.math.BigDecimal;

public interface CustomerMembershipProjection {
    String getUserId();
    BigDecimal getTripPoints();
    Integer getTotalTrips();
    BigDecimal getTotalSpent();
    String getCurrentTierId();
    String getCurrentBadge();
    Integer getPriorityLevel();
    BigDecimal getPointMultiplier();
    Integer getDiscountPercent();
}
