package vn.com.routex.hub.booking.service.infrastructure.cache.redisson.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;
    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();

        String redisAddress = String.format("redis://%s:6379", redisHost);
        config.useSingleServer().setAddress(redisAddress).setDatabase(0);

        return Redisson.create(config);
    }
}
