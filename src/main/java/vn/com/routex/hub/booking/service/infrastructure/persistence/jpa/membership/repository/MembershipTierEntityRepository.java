package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.membership.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.membership.entity.MembershipTierEntity;

import java.util.Optional;

public interface MembershipTierEntityRepository extends JpaRepository<MembershipTierEntity, String> {
    Optional<MembershipTierEntity> findByPriorityLevel(int i);
}
