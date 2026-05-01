package vn.com.routex.hub.booking.service.infrastructure.cache.redis.service.impl;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.booking.service.infrastructure.cache.redis.models.RouteCacheSeat;
import vn.com.routex.hub.booking.service.infrastructure.cache.redis.service.RouteSeatCacheService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteSeatCacheServiceImpl implements RouteSeatCacheService {

    private final RedissonClient redissonClient;

    private static final String ROUTE_SEAT_KEY = "route-seat:%s";
    private static final Duration TTL = Duration.ofMinutes(30);

    @Override
    public void putSeats(String routeId, List<RouteCacheSeat> cacheSeats) {
        String key = String.format(ROUTE_SEAT_KEY, routeId);
        RMap<String, RouteCacheSeat> map = redissonClient.getMap(key);

        Map<String, RouteCacheSeat> seatMap = cacheSeats.stream()
                .collect(Collectors.toMap(
                        RouteCacheSeat::seatNo,
                        seat -> seat
                ));

        map.putAll(seatMap);
        map.expire(TTL);
    }


    @Override
    public List<RouteCacheSeat> getSeats(String routeId) {
        String key = String.format(ROUTE_SEAT_KEY, routeId);

        RMap<String, RouteCacheSeat> map = redissonClient.getMap(key);

        Collection<RouteCacheSeat> values = map.readAllValues();
        return new ArrayList<>(values);
    }

    @Override
    public Map<String, RouteCacheSeat> getSpecificSeat(String routeId, List<String> seatNos) {
        String key = String.format(ROUTE_SEAT_KEY, routeId);
        RMap<String, RouteCacheSeat> map = redissonClient.getMap(key);

        return map.getAll(new HashSet<>(seatNos));
    }

    @Override
    public void updateSeatsStatus(String routeId, List<RouteCacheSeat> cacheSeats) {
        String key = String.format(ROUTE_SEAT_KEY, routeId);
        RMap<String, RouteCacheSeat> map = redissonClient.getMap(key);
        Map<String, RouteCacheSeat> updates = cacheSeats
                .stream()
                .collect(Collectors.toMap(RouteCacheSeat::seatNo, s -> s));

        map.putAll(updates);
    }

    @Override
    public void evictSeat(String routeId) {
        String key = String.format(ROUTE_SEAT_KEY, routeId);
        redissonClient.getBucket(key).delete();
    }
}
