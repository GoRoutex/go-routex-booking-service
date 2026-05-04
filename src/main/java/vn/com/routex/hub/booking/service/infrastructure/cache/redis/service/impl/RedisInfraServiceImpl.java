package vn.com.routex.hub.booking.service.infrastructure.cache.redis.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import tools.jackson.databind.ObjectMapper;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.hub.booking.service.infrastructure.cache.redis.service.RedisInfraService;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RedisInfraServiceImpl implements RedisInfraService {

    private final SystemLog sLog = SystemLog.getLogger(this.getClass());
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void setString(String key, String value) {
        if(!StringUtils.hasLength(key)) {
            return;
        }
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public String getString(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key))
                .map(String::valueOf)
                .orElse(null);
    }

    @Override
    public void setObject(String key, Object value) {
        if(!StringUtils.hasLength(key)) {
            return;
        }

        try {
            redisTemplate.opsForValue().set(key, value);
        } catch(Exception e) {
            sLog.info("setObject error: {}", e.getMessage());
        }
    }

    @Override
    public <T> T getObject(String key, Class<T> targetClass) {

        Object result = redisTemplate.opsForValue().get(key);
        sLog.info("Get Cached: {}", result);
        if(result == null) {
            return null;
        }

        // In case result is a LinkedHashMap

        if(result instanceof Map) {
            try {
                return objectMapper.convertValue(result, targetClass);
            } catch(IllegalArgumentException e) {
                sLog.error("Error converting LinkedHashMap to Object: {}", e.getMessage());
                return null;
            }
        }

        if(result instanceof String) {
            return objectMapper.readValue((String) result, targetClass);
        }
        return null;
    }
}
