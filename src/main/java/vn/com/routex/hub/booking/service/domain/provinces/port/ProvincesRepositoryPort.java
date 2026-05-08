package vn.com.routex.hub.booking.service.domain.provinces.port;



import vn.com.routex.hub.booking.service.domain.provinces.model.Province;

import java.util.Optional;

public interface ProvincesRepositoryPort {
    // Master data
    Optional<Province> findById(Integer id);

    Optional<Province> findByCode(String code);
}

