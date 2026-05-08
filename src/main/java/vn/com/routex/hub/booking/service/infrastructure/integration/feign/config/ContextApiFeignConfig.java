package vn.com.routex.hub.booking.service.infrastructure.integration.feign.config;

import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import vn.com.routex.hub.booking.service.infrastructure.persistence.config.RequestAttributes;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Configuration
public class ContextApiFeignConfig {

    @Bean
    public RequestInterceptor contextApiRequestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                var request = attributes.getRequest();
                copyHeader(requestTemplate, RequestAttributes.REQUEST_ID, request.getHeader(RequestAttributes.REQUEST_ID));
                copyHeader(requestTemplate, RequestAttributes.REQUEST_DATE_TIME, request.getHeader(RequestAttributes.REQUEST_DATE_TIME));
                copyHeader(requestTemplate, RequestAttributes.CHANNEL, request.getHeader(RequestAttributes.CHANNEL));
                copyHeader(requestTemplate, "Authorization", request.getHeader("Authorization"));
            }

            ensureHeader(requestTemplate, RequestAttributes.REQUEST_ID, UUID.randomUUID().toString());
            ensureHeader(requestTemplate, RequestAttributes.REQUEST_DATE_TIME,
                    OffsetDateTime.now(ZoneOffset.ofHours(7)).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
            ensureHeader(requestTemplate, RequestAttributes.CHANNEL, "ONL");
        };
    }

    @Bean
    public ErrorDecoder contextApiErrorDecoder() {
        return new ContextApiErrorDecoder();
    }

    private static void copyHeader(feign.RequestTemplate template, String name, String value) {
        if (value != null && !value.isBlank()) {
            template.header(name, value);
        }
    }

    private static void ensureHeader(feign.RequestTemplate template, String name, String fallbackValue) {
        if (!template.headers().containsKey(name) || template.headers().get(name).isEmpty()) {
            template.header(name, fallbackValue);
        }
    }
}
