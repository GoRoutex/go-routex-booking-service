package vn.com.routex.hub.booking.service.infrastructure.cache.redisson.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;
    @Bean
    public RedissonClient redissonClient(ObjectMapper objectMapper) {
        Config config = new Config();

        String redisAddress = String.format("redis://%s:6379", redisHost);
        config.useSingleServer().setAddress(redisAddress).setDatabase(0);
        config.setCodec(new JsonJacksonCodec(objectMapper));
        return Redisson.create(config);
    }
}
