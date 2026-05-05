package vn.com.routex.hub.booking.service.infrastructure.integration.merchantplatform.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.com.routex.hub.booking.service.infrastructure.integration.feign.config.ContextApiFeignConfig;
import vn.com.routex.hub.booking.service.infrastructure.integration.merchantplatform.dto.FetchTripBookingContextClientResponse;
import vn.com.routex.hub.booking.service.infrastructure.integration.merchantplatform.dto.FetchVehicleSeatBlueprintClientResponse;

@FeignClient(
        name = "merchant-platform-internal-context-client",
        url = "${clients.merchant-platform.base-url}",
        configuration = ContextApiFeignConfig.class
)
public interface MerchantPlatformInternalContextFeignClient {

    @GetMapping("/trips/booking-context")
    FetchTripBookingContextClientResponse fetchTripBookingContext(@RequestParam String tripId);

    @GetMapping("/vehicles/seat-blueprint")
    FetchVehicleSeatBlueprintClientResponse fetchVehicleSeatBlueprint(@RequestParam String vehicleId);
}
