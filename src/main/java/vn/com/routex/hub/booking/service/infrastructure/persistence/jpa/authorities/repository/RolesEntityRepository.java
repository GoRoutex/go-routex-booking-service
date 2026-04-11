package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.authorities.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.authorities.entity.RolesEntity;

import java.util.Optional;

@Repository
public interface RolesEntityRepository extends JpaRepository<RolesEntity, String> {

    Optional<RolesEntity> findByCode(String code);

    boolean existsByCode(String code);
}
