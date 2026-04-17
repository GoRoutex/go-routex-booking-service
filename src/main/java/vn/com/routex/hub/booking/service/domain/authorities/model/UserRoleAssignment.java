package vn.com.routex.hub.booking.service.domain.authorities.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class UserRoleAssignment {
    private String userId;
    private String roleId;
    private OffsetDateTime assignedAt;

    public static UserRoleAssignment assign(String userId, String roleId, OffsetDateTime assignedAt) {
        return UserRoleAssignment.builder()
                .userId(userId)
                .roleId(roleId)
                .assignedAt(assignedAt)
                .build();
    }
}
