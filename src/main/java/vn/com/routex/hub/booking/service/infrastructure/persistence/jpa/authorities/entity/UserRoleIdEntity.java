package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.authorities.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleIdEntity implements Serializable {

    private String userId;
    private String roleId;
}
