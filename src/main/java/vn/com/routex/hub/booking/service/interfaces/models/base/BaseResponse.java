package vn.com.routex.hub.booking.service.interfaces.models.base;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.booking.service.interfaces.models.result.ApiResult;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BaseResponse<T> {
    private String requestId;
    private String requestDateTime;
    private String channel;
    private ApiResult result;
    private T data;
}
