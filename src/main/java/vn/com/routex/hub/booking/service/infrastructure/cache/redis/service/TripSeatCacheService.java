package vn.com.routex.hub.booking.service.infrastructure.cache.redis.service;

import vn.com.routex.hub.booking.service.infrastructure.cache.redis.models.TripCacheSeat;

import java.util.List;
import java.util.Map;

public interface TripSeatCacheService {

    void putSeats(String tripId, List<TripCacheSeat> cacheSeats);
    List<TripCacheSeat> getSeats(String tripId);
    Map<String, TripCacheSeat> getSpecificSeat(String tripId, List<String> seatNos);
    void updateSeatsStatus(String tripId, List<TripCacheSeat> cacheSeats);
    void evictSeat(String tripId);


}
