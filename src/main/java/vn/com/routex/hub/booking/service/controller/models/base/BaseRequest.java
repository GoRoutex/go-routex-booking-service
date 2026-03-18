package vn.com.routex.hub.booking.service.controller.models.base;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ApplicationConstant.CHANNEL_REGREX;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ApplicationConstant.DATETIME_REGREX;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ApplicationConstant.REQUEST_ID_REGREX;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class BaseRequest {

    @NotBlank
    @Pattern(regexp = REQUEST_ID_REGREX, message = "RequestId must be in xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx")
    private String requestId;

    @NotBlank
    @Pattern(regexp = DATETIME_REGREX, message = "RequestDateTime must be in yyyy-MM-dd'T'HH:mm:ss.SSS format (e.g: 2025-11-01T16:27:00.135+7:00)")
    private String requestDateTime;

    @NotBlank
    @Size(max = 30, message = "Channel can't exceed 30 characters")
    @Pattern(regexp = CHANNEL_REGREX, message = "Channel must be ONL, OFF")
    private String channel;
}

