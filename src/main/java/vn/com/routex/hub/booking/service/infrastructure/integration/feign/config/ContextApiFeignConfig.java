package vn.com.routex.hub.booking.service.infrastructure.integration.feign.config;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContextApiFeignConfig {

    @Bean
    public ErrorDecoder contextApiErrorDecoder() {
        return new ContextApiErrorDecoder();
    }
}
