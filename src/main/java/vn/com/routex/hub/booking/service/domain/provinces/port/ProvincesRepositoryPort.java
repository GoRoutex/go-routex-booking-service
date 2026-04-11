package vn.com.routex.hub.booking.service.domain.provinces.port;



import vn.com.routex.hub.booking.service.domain.provinces.model.Province;

import java.util.Optional;

public interface ProvincesRepositoryPort {
    // Master data
    Optional<Province> findById(Integer id);

    Optional<Province> findByCode(String code);

    // Mapping
    boolean isAssigned(Integer provinceId, String merchantId);

    void assign(Integer provinceId, String merchantId);

    void unassign(Integer provinceId, String merchantId);
}

