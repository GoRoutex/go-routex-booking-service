package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.idempotency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.idempotency.entity.ProcessedEventEntity;

@Repository
public interface ProcessedEventEntityRepository extends JpaRepository<ProcessedEventEntity, String> {
}
