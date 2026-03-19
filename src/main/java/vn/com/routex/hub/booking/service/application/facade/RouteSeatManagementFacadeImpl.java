package vn.com.routex.hub.booking.service.application.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.application.services.GetAllSeatService;
import vn.com.routex.hub.booking.service.application.services.HoldSeatService;
import vn.com.routex.hub.booking.service.controller.models.seat.GetAllSeatRequest;
import vn.com.routex.hub.booking.service.controller.models.seat.GetAllSeatResponse;
import vn.com.routex.hub.booking.service.controller.models.seat.HoldSeatRequest;
import vn.com.routex.hub.booking.service.controller.models.seat.HoldSeatResponse;
import vn.com.routex.hub.booking.service.infrastructure.persistence.utils.HttpResponseUtil;

@Component
@RequiredArgsConstructor
public class RouteSeatManagementFacadeImpl implements RouteSeatManagementFacade{

    private final GetAllSeatService getAllSeatService;
    private final HoldSeatService holdSeatService;

    @Override
    public ResponseEntity<GetAllSeatResponse> getAllSeat(GetAllSeatRequest request) {
        GetAllSeatResponse response = getAllSeatService.getAllSeat(request);
        return HttpResponseUtil.buildResponse(request, response);
    }

    @Override
    public ResponseEntity<HoldSeatResponse> holdSeat(HoldSeatRequest request) {
        HoldSeatResponse response = holdSeatService.holdSeat(request);
        return HttpResponseUtil.buildResponse(request, response);
    }
}
