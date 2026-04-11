package vn.com.routex.hub.booking.service.domain.authorities.port;


import vn.com.routex.hub.booking.service.domain.authorities.model.PermissionProfile;

import java.util.List;
import java.util.Set;

public interface PermissionRepositoryPort {
    boolean existsByCode(String code);

    List<PermissionProfile> findByCodes(Set<String> codes);

    void save(PermissionProfile permissionProfile);
}
