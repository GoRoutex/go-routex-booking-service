package vn.com.routex.hub.booking.service.infrastructure.integration.feign.config;

import feign.FeignException;
import feign.Request;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant;
import vn.com.routex.hub.booking.service.infrastructure.persistence.exception.CustomFeignException;
import vn.com.routex.hub.booking.service.infrastructure.persistence.utils.ExceptionUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;

public class ContextApiErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 404) {
            return FeignException.errorStatus(methodKey, response);
        }

        Request request = response.request();
        String requestId = firstHeader(request.headers(), "requestId");
        String requestDateTime = firstHeader(request.headers(), "requestDateTime");
        String channel = firstHeader(request.headers(), "channel");
        String responseCode = isTimeout(response.status()) ? ErrorConstant.TIMEOUT_ERROR : ErrorConstant.SYSTEM_ERROR;
        String description = isTimeout(response.status())
                ? ErrorConstant.TIMEOUT_ERROR_MESSAGE
                : String.format(
                        "Feign call failed. method=%s, status=%s, reason=%s, url=%s, body=%s",
                        methodKey,
                        response.status(),
                        defaultString(response.reason()),
                        request.url(),
                        extractBody(response)
                );

        return new CustomFeignException(
                requestId,
                requestDateTime,
                channel,
                ExceptionUtils.buildResultResponse(responseCode, description),
                extractTargetService(methodKey),
                response.status(),
                description,
                defaultErrorDecoder.decode(methodKey, response)
        );
    }

    private boolean isTimeout(int httpStatus) {
        return httpStatus == 408 || httpStatus == 504;
    }

    private String firstHeader(Map<String, Collection<String>> headers, String key) {
        Collection<String> values = headers.get(key);
        if (values == null || values.isEmpty()) {
            return null;
        }
        return values.iterator().next();
    }

    private String extractBody(Response response) {
        if (response.body() == null) {
            return "";
        }
        try {
            return Util.toString(response.body().asReader(StandardCharsets.UTF_8));
        } catch (IOException ex) {
            return "<unreadable-body>";
        }
    }

    private String extractTargetService(String methodKey) {
        int separatorIndex = methodKey.indexOf('#');
        if (separatorIndex <= 0) {
            return methodKey;
        }
        return methodKey.substring(0, separatorIndex);
    }

    private String defaultString(String value) {
        return value == null || value.isBlank() ? "N/A" : value;
    }
}
