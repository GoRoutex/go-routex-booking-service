package vn.com.routex.hub.booking.service.domain.membership;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MembershipTierGroup {
    BRONZE("MBR_BRONZE", "BRONZE"),
    SILVER("MBR_SILVER", "SILVER"),
    GOLD("MBR_GOLD", "GOLD"),
    PLATINUM("MBR_PLATINUM", "PLATINUM"),
    VIP("MBR_VIP", "VIP");

    private final String id;
    private final String badge;
}
