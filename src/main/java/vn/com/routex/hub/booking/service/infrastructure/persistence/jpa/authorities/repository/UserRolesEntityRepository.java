package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.authorities.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.authorities.entity.UserRoleIdEntity;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.authorities.entity.UserRolesEntity;

import java.util.List;

@Repository
public interface UserRolesEntityRepository extends JpaRepository<UserRolesEntity, UserRoleIdEntity> {

    List<UserRolesEntity> findByIdUserId(String userId);

    void deleteByIdUserId(String userId);
}
