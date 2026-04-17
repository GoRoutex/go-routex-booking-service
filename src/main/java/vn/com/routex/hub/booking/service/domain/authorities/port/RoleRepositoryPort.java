package vn.com.routex.hub.booking.service.domain.authorities.port;



import vn.com.routex.hub.booking.service.domain.authorities.model.RoleAggregate;

import java.util.Optional;

public interface RoleRepositoryPort {
    boolean existsByCode(String code);

    Optional<RoleAggregate> findById(String roleId);

    Optional<RoleAggregate> findByCode(String code);

    void save(RoleAggregate roleAggregate);
}
