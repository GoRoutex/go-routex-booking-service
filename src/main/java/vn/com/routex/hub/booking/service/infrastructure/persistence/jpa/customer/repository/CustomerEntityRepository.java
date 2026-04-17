package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.customer.entity.CustomerEntity;

import java.util.Optional;

public interface CustomerEntityRepository extends JpaRepository<CustomerEntity, String> {
    Optional<CustomerEntity> findByUserId(String userId);
}
