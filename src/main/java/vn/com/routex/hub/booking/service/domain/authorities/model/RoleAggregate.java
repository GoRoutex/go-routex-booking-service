package vn.com.routex.hub.booking.service.domain.authorities.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class RoleAggregate {
    private String id;
    private String code;
    private String name;
    private String description;
    private Boolean enabled;
    private OffsetDateTime createdAt;
    private String createdBy;
    private Set<String> authorityCodes;

    public static RoleAggregate create(
            String id,
            String code,
            String name,
            String description,
            boolean enabled,
            String creator,
            OffsetDateTime createdAt
    ) {
        return RoleAggregate.builder()
                .id(id)
                .code(code)
                .name(name)
                .description(description)
                .enabled(enabled)
                .createdAt(createdAt)
                .createdBy(creator)
                .authorityCodes(new HashSet<>())
                .build();
    }

    public void assignAuthorities(Set<String> authorities) {
        this.authorityCodes = new HashSet<>(authorities);
    }
}
