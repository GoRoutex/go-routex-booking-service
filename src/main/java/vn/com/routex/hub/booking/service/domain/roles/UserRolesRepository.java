package vn.com.routex.hub.booking.service.domain.roles;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRolesRepository extends JpaRepository<UserRoles, UserRoleId> {

    List<UserRoles> findByIdUserId(String userId);
}
