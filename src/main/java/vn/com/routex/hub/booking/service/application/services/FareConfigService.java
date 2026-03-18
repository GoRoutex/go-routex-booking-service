package vn.com.routex.hub.booking.service.application.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.booking.service.domain.fare.FareConfig;
import vn.com.routex.hub.booking.service.domain.fare.FareConfigRepository;
import vn.com.routex.hub.booking.service.domain.vehicle.VehicleType;
import vn.com.routex.hub.booking.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.booking.service.infrastructure.persistence.utils.ExceptionUtils;

import java.math.BigDecimal;

import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.FLARE_CONFIG_NOT_FOUND;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class FareConfigService {

    private final FareConfigRepository flareConfigRepository;

    public BigDecimal getUnitPrice(VehicleType vehicleType) {
        FareConfig fareConfig = flareConfigRepository.findByVehicleType(vehicleType.name())
                .orElseThrow(() -> new BusinessException(ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, FLARE_CONFIG_NOT_FOUND)));

        return fareConfig.getBasePrice();
    }
}
