package vn.com.routex.hub.booking.service.domain.customer.port;



import vn.com.routex.hub.booking.service.domain.customer.model.CustomerMembership;

import java.util.Optional;

public interface CustomerMembershipRepositoryPort {
    Optional<CustomerMembership> findById(String id);

    CustomerMembership save(CustomerMembership customerMembership);
}
