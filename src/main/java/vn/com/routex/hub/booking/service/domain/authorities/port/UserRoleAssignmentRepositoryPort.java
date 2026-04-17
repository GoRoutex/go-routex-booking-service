package vn.com.routex.hub.booking.service.domain.authorities.port;


import vn.com.routex.hub.booking.service.domain.authorities.model.UserRoleAssignment;

public interface UserRoleAssignmentRepositoryPort {
    boolean exists(String userId, String roleId);

    void save(UserRoleAssignment assignment);

    void deleteByUserId(String userId);
}
