package vn.com.routex.hub.booking.service.infrastructure.integration.payment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vn.com.routex.hub.booking.service.infrastructure.integration.feign.config.ContextApiFeignConfig;
import vn.com.routex.hub.booking.service.infrastructure.integration.payment.dto.FetchPaymentContextClientRequest;
import vn.com.routex.hub.booking.service.infrastructure.integration.payment.dto.FetchPaymentContextClientResponse;

@FeignClient(
        name = "payment-service-context-client",
        url = "${clients.payment-service.base-url}",
        configuration = ContextApiFeignConfig.class
)
public interface PaymentServiceContextFeignClient {

    @PostMapping("/api/v1/payment-service/payments/context")
    FetchPaymentContextClientResponse fetchPaymentContext(@RequestBody FetchPaymentContextClientRequest request);
}
