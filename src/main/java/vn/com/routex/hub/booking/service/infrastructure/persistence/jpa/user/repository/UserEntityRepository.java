package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.user.entity.UserEntity;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, String> {
}
