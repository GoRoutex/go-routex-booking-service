package vn.com.routex.hub.booking.service.domain.merchant.port;


import vn.com.routex.hub.booking.service.domain.common.PagedResult;
import vn.com.routex.hub.booking.service.domain.merchant.model.MerchantApplicationForm;

import java.util.Optional;

public interface MerchantApplicationFormRepositoryPort {

    MerchantApplicationForm save(MerchantApplicationForm merchantApplicationForm);

    boolean existsByFormCode(String formCode);

    String generateFormCode();

    Optional<MerchantApplicationForm> findById(String id);

    PagedResult<MerchantApplicationForm> fetch(int pageNumber, int pageSize);
}
