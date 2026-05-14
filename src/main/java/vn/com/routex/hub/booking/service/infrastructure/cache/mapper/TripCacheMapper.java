package vn.com.routex.hub.booking.service.infrastructure.cache.mapper;


import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.domain.seat.model.TripSeat;
import vn.com.routex.hub.booking.service.infrastructure.cache.redis.models.TripCacheSeat;

@Component
public class TripCacheMapper {

    public TripCacheSeat toCacheModel(TripSeat domain) {
        if(domain == null) {
            return null;
        }

        return TripCacheSeat.builder()
                .tripId(domain.getTripId())
                .seatId(domain.getId())
                .seatNo(domain.getSeatNo())
                .seatTemplateId(domain.getSeatTemplateId())
                .status(domain.getStatus())
                .build();
    }

    public TripSeat toDomain(TripCacheSeat cache) {
        if(cache == null) {
            return null;
        }

        return TripSeat.builder()
                .id(cache.seatId())
                .tripId(cache.tripId())
                .seatNo(cache.seatNo())
                .status(cache.status())
                .seatTemplateId(cache.seatTemplateId())
                .build();
    }
}
