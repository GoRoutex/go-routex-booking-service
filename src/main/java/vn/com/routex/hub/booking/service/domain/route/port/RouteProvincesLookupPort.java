package vn.com.routex.hub.booking.service.domain.route.port;


import vn.com.routex.hub.booking.service.domain.route.model.ProvincesCodePair;

public interface RouteProvincesLookupPort {
    ProvincesCodePair getCodes(String origin, String destination);
}
