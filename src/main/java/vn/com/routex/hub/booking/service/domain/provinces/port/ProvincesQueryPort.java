package vn.com.routex.hub.booking.service.domain.provinces.port;


import vn.com.routex.hub.booking.service.domain.common.PagedResult;
import vn.com.routex.hub.booking.service.domain.provinces.readmodel.ProvincesFetchView;
import vn.com.routex.hub.booking.service.domain.provinces.readmodel.ProvincesSearchItem;

import java.util.List;

public interface ProvincesQueryPort {
    List<ProvincesSearchItem> search(String merchantId, String keyword, int page, int size);


    PagedResult<ProvincesFetchView> fetchRoutes(String merchantId, int pageNumber, int pageSize);
}
