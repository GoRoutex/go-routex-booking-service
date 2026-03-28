package vn.com.routex.hub.booking.service.infrastructure.persistence.adapter.fare;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.domain.fare.port.FareConfigPort;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.fare.repository.FareConfigEntityRepository;

import java.math.BigDecimal;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FareConfigRepositoryAdapter implements FareConfigPort {

    private final FareConfigEntityRepository fareConfigJpaRepository;

    @Override
    public Optional<BigDecimal> findBasePriceByVehicleType(String vehicleType) {
        return fareConfigJpaRepository.findByVehicleType(vehicleType)
                .map(fareConfig -> fareConfig.getBasePrice());
    }
}
