package vn.com.routex.hub.booking.service.application.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.application.services.RouteSeatManagementService;
import vn.com.routex.hub.booking.service.infrastructure.persistence.utils.HttpResponseUtil;
import vn.com.routex.hub.booking.service.interfaces.models.seat.GetAllSeatRequest;
import vn.com.routex.hub.booking.service.interfaces.models.seat.GetAllSeatResponse;
import vn.com.routex.hub.booking.service.interfaces.models.seat.HoldSeatRequest;
import vn.com.routex.hub.booking.service.interfaces.models.seat.HoldSeatResponse;

@Component
@RequiredArgsConstructor
public class RouteSeatManagementFacadeImpl implements RouteSeatManagementFacade{

    private final RouteSeatManagementService routeSeatManagementService;

    @Override
    public ResponseEntity<GetAllSeatResponse> getAllSeat(GetAllSeatRequest request) {
        GetAllSeatResponse response = routeSeatManagementService.getAllSeat(request);
        return HttpResponseUtil.buildResponse(request, response);
    }

    @Override
    public ResponseEntity<HoldSeatResponse> holdSeat(HoldSeatRequest request) {
        HoldSeatResponse response = routeSeatManagementService.holdSeat(request);
        return HttpResponseUtil.buildResponse(request, response);
    }
}
