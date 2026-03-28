package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.assignment.entity.RouteAssignmentEntity;

@Repository
public interface RouteAssignmentEntityRepository extends JpaRepository<RouteAssignmentEntity, String> {
}
