package vn.com.routex.hub.booking.service.infrastructure.cache.redis.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.stereotype.Service;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.hub.booking.service.infrastructure.cache.redis.models.TripCacheSeat;
import vn.com.routex.hub.booking.service.infrastructure.cache.redis.service.TripSeatCacheService;

import java.time.Duration;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripSeatCacheServiceImpl implements TripSeatCacheService {

    private final RedissonClient redissonClient;
    private final ObjectMapper objectMapper;
    private static final String TRIP_SEAT_KEY = "trip-seat:%s";
    private final SystemLog sLog = SystemLog.getLogger(this.getClass());
    private static final Duration TTL = Duration.ofMinutes(30);

    @Override
    public void putSeats(String tripId, List<TripCacheSeat> cacheSeats) {
        String key = String.format(TRIP_SEAT_KEY, tripId);
        // Use StringCodec to store values as raw JSON
        RMap<String, String> map = redissonClient.getMap(key, StringCodec.INSTANCE);

        Map<String, String> seatMap = cacheSeats.stream()
                .collect(Collectors.toMap(
                        TripCacheSeat::seatNo,
                        seat -> {
                            try {
                                return objectMapper.writeValueAsString(seat);
                            } catch (Exception e) {
                                throw new RuntimeException("Error serializing seat", e);
                            }
                        }
                ));

        map.putAll(seatMap);
        map.expire(TTL);
    }


    @Override
    public List<TripCacheSeat> getSeats(String tripId) {
        String key = String.format(TRIP_SEAT_KEY, tripId);

        RMap<String, String> map = redissonClient.getMap(key, StringCodec.INSTANCE);

        Collection<String> values = map.readAllValues();
        return values.stream()
                .map(v -> {
                    try {
                        return objectMapper.readValue(v, TripCacheSeat.class);
                    } catch (Exception e) {
                        throw new RuntimeException("Error deserializing seat", e);
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, TripCacheSeat> getSpecificSeat(String tripId, List<String> seatNos) {
        String key = String.format(TRIP_SEAT_KEY, tripId);
        RMap<String, String> map = redissonClient.getMap(key, StringCodec.INSTANCE);

        Map<String, String> rawMap = map.getAll(new HashSet<>(seatNos));
        return rawMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> {
                            try {
                                return objectMapper.readValue(e.getValue(), TripCacheSeat.class);
                            } catch (Exception ex) {
                                throw new RuntimeException("Error deserializing seat", ex);
                            }
                        }
                ));
    }

    @Override
    public void updateSeatsStatus(String tripId, List<TripCacheSeat> cacheSeats) {
        String key = String.format(TRIP_SEAT_KEY, tripId);
        RMap<String, String> map = redissonClient.getMap(key, StringCodec.INSTANCE);

        Set<String> seatNumbers = cacheSeats.stream()
                .map(TripCacheSeat::seatNo)
                .collect(Collectors.toSet());

        Map<String, String> existingSeatMap = map.getAll(seatNumbers);

        Map<String, String> updates = cacheSeats.stream()
                .filter(seat -> existingSeatMap.containsKey(seat.seatNo()))
                .collect(Collectors.toMap(
                        TripCacheSeat::seatNo,
                        seat -> {
                            try {
                                String json = existingSeatMap.get(seat.seatNo());
                                TripCacheSeat existing = objectMapper.readValue(json, TripCacheSeat.class);

                                TripCacheSeat mergedSeat = existing.toBuilder()
                                        .status(seat.status())
                                        .build();
                                return objectMapper.writeValueAsString(mergedSeat);
                            } catch(Exception e) {
                                throw new RuntimeException("Error deserializing seat: ", e);
                            }
                        }
                ));
        if(!updates.isEmpty()) {
            map.putAll(updates);
        }
    }

    @Override
    public void evictSeat(String tripId) {
        String key = String.format(TRIP_SEAT_KEY, tripId);
        redissonClient.getBucket(key).delete();
    }
}
