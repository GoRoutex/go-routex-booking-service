package vn.com.routex.hub.booking.service.infrastructure.integration.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FetchCustomerByUserIdClientResponseData {
    private String id;
    private String userId;
    private String fullName;
}
