package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.operation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.operation.entity.OperationPointEntity;

@Repository
public interface OperationPointEntityRepository extends JpaRepository<OperationPointEntity, String> {
}

