package vn.com.routex.hub.booking.service.infrastructure.integration.userservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.com.routex.hub.booking.service.infrastructure.integration.feign.config.ContextApiFeignConfig;
import vn.com.routex.hub.booking.service.infrastructure.integration.userservice.dto.FetchCustomerByUserIdClientResponse;

@FeignClient(
        name = "user-service-internal-context-client",
        url = "${clients.user-service.base-url}",
        configuration = ContextApiFeignConfig.class
)
public interface UserServiceInternalContextFeignClient {

    @GetMapping("/customers/detail-by-user-id")
    FetchCustomerByUserIdClientResponse fetchCustomerByUserId(@RequestParam String userId);
}
