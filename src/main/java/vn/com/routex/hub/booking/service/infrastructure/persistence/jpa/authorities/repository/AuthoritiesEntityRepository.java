package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.authorities.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.authorities.entity.AuthoritiesEntity;

import java.util.List;
import java.util.Set;

@Repository
public interface AuthoritiesEntityRepository extends JpaRepository<AuthoritiesEntity, Integer> {
    boolean existsByCode(String code);

    List<AuthoritiesEntity> findByCodeIn(Set<String> authoritiesCode);
}
