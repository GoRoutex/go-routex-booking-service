package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.customer.entity.CustomerEntity;

@Repository
public interface CustomerEntityRepository extends JpaRepository<CustomerEntity, String> {
}
