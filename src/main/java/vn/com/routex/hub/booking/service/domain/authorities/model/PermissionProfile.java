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
public class PermissionProfile {
    private int id;
    private String code;
    private String name;
    private String description;
    private Boolean enabled;
    private OffsetDateTime createdAt;
    private String createdBy;

    public static PermissionProfile create(
            String code,
            String name,
            String description,
            boolean enabled,
            String creator,
            OffsetDateTime createdAt
    ) {
        return PermissionProfile.builder()
                .code(code)
                .name(name)
                .description(description)
                .enabled(enabled)
                .createdAt(createdAt)
                .createdBy(creator)
                .build();
    }
}
