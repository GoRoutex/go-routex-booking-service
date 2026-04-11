package vn.com.routex.hub.booking.service.domain.authorities.port;


import vn.com.routex.hub.booking.service.domain.authorities.model.UserAccountReference;

import java.util.Optional;

public interface UserAccountLookupPort {
    Optional<UserAccountReference> findById(String userId);

    Optional<UserAccountReference> findByEmail(String email);
}
