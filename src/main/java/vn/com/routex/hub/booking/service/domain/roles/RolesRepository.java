package vn.com.routex.hub.booking.service.domain.roles;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<Roles, String> {

    Optional<Roles> findByCode(String code);

    boolean existsByCode(String code);
}
