package vn.com.routex.hub.booking.service.domain.department.port;



import vn.com.routex.hub.booking.service.domain.common.PagedResult;
import vn.com.routex.hub.booking.service.domain.department.model.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepositoryPort {

    Optional<Department> findById(String id);

    Optional<Department> findById(String id, String merchantId);

    List<Department> findByMerchantId(String merchantId);

    void save(Department department);

    PagedResult<Department> fetch(int pageNumber, int pageSize);

    PagedResult<Department> fetch(String merchantId, int pageNumber, int pageSize);
}
