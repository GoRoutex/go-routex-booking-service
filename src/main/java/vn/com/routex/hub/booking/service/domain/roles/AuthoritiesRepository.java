package vn.com.routex.hub.booking.service.domain.roles;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface AuthoritiesRepository extends JpaRepository<Authorities, Integer> {
    boolean existsByCode(String code);

    List<Authorities> findByCodeIn(Set<String> authoritiesCode);
}
