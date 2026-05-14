package vn.com.routex.hub.booking.service.infrastructure.integration.merchantplatform.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vn.com.routex.hub.booking.service.infrastructure.integration.feign.config.ContextApiFeignConfig;
import vn.com.routex.hub.booking.service.infrastructure.integration.merchantplatform.dto.CreateTicketClientRequest;
import vn.com.routex.hub.booking.service.infrastructure.integration.merchantplatform.dto.CreateTicketClientResponse;

@FeignClient(
        name = "merchant-ticket-client",
        url = "${clients.merchant-platform.base-url}",
        configuration = ContextApiFeignConfig.class
)
public interface MerchantTicketFeignClient {

    @PostMapping("/tickets/create")
    CreateTicketClientResponse createTickets(@RequestBody CreateTicketClientRequest request);
}

