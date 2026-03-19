package vn.com.routex.hub.booking.service.infrastructure.cache.redis.service.impl;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.booking.service.infrastructure.cache.redis.models.RouteCacheSeat;
import vn.com.routex.hub.booking.service.infrastructure.cache.redis.service.RouteSeatCacheService;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteSeatCacheServiceImpl implements RouteSeatCacheService {

    private final RedissonClient redissonClient;

    private static final String ROUTE_SEAT_KEY = "route-seat:%s";
    private static final Duration TTL = Duration.ofMinutes(30);

    @Override
    public void putSeats(String routeId, List<RouteCacheSeat> cacheSeats) {
        String key = String.format(ROUTE_SEAT_KEY, routeId);
        RBucket<List<RouteCacheSeat>> bucket = redissonClient.getBucket(key);
        bucket.set(cacheSeats, TTL);
    }

    @Override
    public List<RouteCacheSeat> getSeats(String routeId) {
        String key = String.format(ROUTE_SEAT_KEY, routeId);
        RBucket<List<RouteCacheSeat>> bucket = redissonClient.getBucket(key);
        return bucket.get();
    }

    @Override
    public void evictSeat(String routeId) {
        String key = String.format(ROUTE_SEAT_KEY, routeId);
        redissonClient.getBucket(key).delete();
    }
}
