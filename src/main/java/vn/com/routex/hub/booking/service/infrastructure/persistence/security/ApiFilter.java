package vn.com.routex.hub.booking.service.infrastructure.persistence.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;
import vn.com.routex.hub.booking.service.infrastructure.persistence.config.RequestAttributes;
import vn.com.routex.hub.booking.service.controller.models.base.BaseRequest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApiFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        if (requestURI.startsWith("/actuator/") || requestURI.contains("/location-service/")|| requestURI.startsWith("/swagger-ui") || requestURI.startsWith("/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            CachedHttpServletRequestWrapper cachedHttpServletRequestWrapper = new CachedHttpServletRequestWrapper(request);
            ContentCachingResponseWrapper contentCachingResponseWrapper = new ContentCachingResponseWrapper(response);
            String jsonStringBody = new String(cachedHttpServletRequestWrapper.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            BaseRequest apiRequest = objectMapper.readValue(jsonStringBody, BaseRequest.class);
            request.setAttribute(RequestAttributes.REQUEST_ID, apiRequest.getRequestId());
            request.setAttribute(RequestAttributes.REQUEST_DATE_TIME, apiRequest.getRequestDateTime());
            request.setAttribute(RequestAttributes.CHANNEL, apiRequest.getChannel());
            filterChain.doFilter(cachedHttpServletRequestWrapper, contentCachingResponseWrapper);
            String responseMessage = new String(contentCachingResponseWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
            log.info("{}", responseMessage);
            contentCachingResponseWrapper.copyBodyToResponse();
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid Request");
            response.getWriter().flush();
        }
    }
}
