package vn.com.routex.hub.booking.service.domain.merchant.port;


import vn.com.routex.hub.booking.service.domain.merchant.model.Merchant;

import java.util.Optional;

public interface MerchantRepositoryPort {

    Merchant save(Merchant merchant);

    Optional<Merchant> findById(String merchantId);

    boolean existsByCode(String code);

    String generateMerchantCode();
}
