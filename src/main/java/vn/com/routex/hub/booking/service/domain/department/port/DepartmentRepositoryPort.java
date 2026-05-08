package vn.com.routex.hub.booking.service.domain.department.port;



import vn.com.routex.hub.booking.service.domain.common.PagedResult;
import vn.com.routex.hub.booking.service.domain.department.model.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepositoryPort {
    Optional<Department> findByCode(String code);

    Optional<Department> findByCode(String code, String merchantId);

    Optional<Department> findById(String id);

    Optional<Department> findById(String id, String merchantId);

    boolean existsByCode(String code);

    boolean existsByCode(String code, String merchantId);

    List<Department> findByMerchantId(String merchantId);

    void save(Department department);

    PagedResult<Department> fetch(int pageNumber, int pageSize);

    PagedResult<Department> fetch(String merchantId, int pageNumber, int pageSize);
}
