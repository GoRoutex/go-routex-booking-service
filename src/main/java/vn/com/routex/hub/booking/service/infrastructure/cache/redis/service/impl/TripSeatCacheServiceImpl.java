package vn.com.routex.hub.booking.service.infrastructure.cache.redis.service.impl;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.booking.service.infrastructure.cache.redis.models.TripCacheSeat;
import vn.com.routex.hub.booking.service.infrastructure.cache.redis.service.TripSeatCacheService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripSeatCacheServiceImpl implements TripSeatCacheService {

    private final RedissonClient redissonClient;

    private static final String ROUTE_SEAT_KEY = "route-seat:%s";
    private static final Duration TTL = Duration.ofMinutes(30);

    @Override
    public void putSeats(String tripId, List<TripCacheSeat> cacheSeats) {
        String key = String.format(ROUTE_SEAT_KEY, tripId);
        RMap<String, TripCacheSeat> map = redissonClient.getMap(key);

        Map<String, TripCacheSeat> seatMap = cacheSeats.stream()
                .collect(Collectors.toMap(
                        TripCacheSeat::seatNo,
                        seat -> seat
                ));

        map.putAll(seatMap);
        map.expire(TTL);
    }


    @Override
    public List<TripCacheSeat> getSeats(String tripId) {
        String key = String.format(ROUTE_SEAT_KEY, tripId);

        RMap<String, TripCacheSeat> map = redissonClient.getMap(key);

        Collection<TripCacheSeat> values = map.readAllValues();
        return new ArrayList<>(values);
    }

    @Override
    public Map<String, TripCacheSeat> getSpecificSeat(String tripId, List<String> seatNos) {
        String key = String.format(ROUTE_SEAT_KEY, tripId);
        RMap<String, TripCacheSeat> map = redissonClient.getMap(key);

        return map.getAll(new HashSet<>(seatNos));
    }

    @Override
    public void updateSeatsStatus(String tripId, List<TripCacheSeat> cacheSeats) {
        String key = String.format(ROUTE_SEAT_KEY, tripId);
        RMap<String, TripCacheSeat> map = redissonClient.getMap(key);
        Map<String, TripCacheSeat> updates = cacheSeats
                .stream()
                .collect(Collectors.toMap(TripCacheSeat::seatNo, s -> s));

        map.putAll(updates);
    }

    @Override
    public void evictSeat(String tripId) {
        String key = String.format(ROUTE_SEAT_KEY, tripId);
        redissonClient.getBucket(key).delete();
    }
}
