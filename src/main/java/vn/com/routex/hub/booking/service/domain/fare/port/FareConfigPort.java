package vn.com.routex.hub.booking.service.domain.fare.port;

import java.math.BigDecimal;
import java.util.Optional;

public interface FareConfigPort {
    Optional<BigDecimal> findBasePriceByVehicleType(String vehicleType);
}
