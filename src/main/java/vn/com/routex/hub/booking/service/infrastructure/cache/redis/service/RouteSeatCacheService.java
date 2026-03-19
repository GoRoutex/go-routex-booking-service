package vn.com.routex.hub.booking.service.infrastructure.cache.redis.service;

import vn.com.routex.hub.booking.service.infrastructure.cache.redis.models.RouteCacheSeat;

import java.util.List;

public interface RouteSeatCacheService {

    void putSeats(String routeId, List<RouteCacheSeat> cacheSeats);
    List<RouteCacheSeat> getSeats(String routeId);
    void evictSeat(String routeId);


}
