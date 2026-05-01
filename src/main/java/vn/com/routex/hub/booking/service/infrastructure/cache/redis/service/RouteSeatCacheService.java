package vn.com.routex.hub.booking.service.infrastructure.cache.redis.service;

import vn.com.routex.hub.booking.service.infrastructure.cache.redis.models.RouteCacheSeat;

import java.util.List;
import java.util.Map;

public interface RouteSeatCacheService {

    void putSeats(String routeId, List<RouteCacheSeat> cacheSeats);
    List<RouteCacheSeat> getSeats(String routeId);
    Map<String, RouteCacheSeat> getSpecificSeat(String routeId, List<String> seatNos);
    void updateSeatsStatus(String routeId, List<RouteCacheSeat> cacheSeats);
    void evictSeat(String routeId);


}
