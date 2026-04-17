package vn.com.routex.hub.booking.service.domain.route.model;

import lombok.Builder;

@Builder
public record ProvincesCodePair(String originCode, String destinationCode) {
}
