package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.route.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.route.entity.RouteEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouteEntityRepository extends JpaRepository<RouteEntity, String>, JpaSpecificationExecutor<RouteEntity> {

    Optional<RouteEntity> findByIdAndMerchantId(String id, String merchantId);

    List<RouteEntity> findByMerchantId(String merchantId);
}