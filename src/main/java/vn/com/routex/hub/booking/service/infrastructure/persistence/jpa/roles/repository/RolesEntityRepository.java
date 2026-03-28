package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.roles.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.roles.entity.RolesEntity;

@Repository
public interface RolesEntityRepository extends JpaRepository<RolesEntity, String> {
}
