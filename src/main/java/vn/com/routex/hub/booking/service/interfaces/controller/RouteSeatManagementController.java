package vn.com.routex.hub.booking.service.interfaces.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.routex.hub.booking.service.application.facade.RouteSeatManagementFacade;
import vn.com.routex.hub.booking.service.interfaces.models.seat.GetAllSeatRequest;
import vn.com.routex.hub.booking.service.interfaces.models.seat.GetAllSeatResponse;
import vn.com.routex.hub.booking.service.interfaces.models.seat.HoldSeatRequest;
import vn.com.routex.hub.booking.service.interfaces.models.seat.HoldSeatResponse;

import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ApiConstant.HOLD_SEAT_PATH;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ApiConstant.MANAGEMENT_PATH;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ApiConstant.ROUTE_SERVICE;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ApiConstant.SEAT_PATH;

@RestController
@RequestMapping(API_PATH + API_VERSION + MANAGEMENT_PATH)
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('route-seat:management') and hasRole('ADMIN')")
public class RouteSeatManagementController {

    private final RouteSeatManagementFacade routeSeatManagementFacade;

    @PostMapping(ROUTE_SERVICE + SEAT_PATH)
    public ResponseEntity<GetAllSeatResponse> getAvailableSeat(@Valid @RequestBody GetAllSeatRequest request) {
        return routeSeatManagementFacade.getAllSeat(request);
    }

    @PostMapping(ROUTE_SERVICE + HOLD_SEAT_PATH)
    public ResponseEntity<HoldSeatResponse> holdSeat(@Valid @RequestBody HoldSeatRequest request) {
        return routeSeatManagementFacade.holdSeat(request);
    }
}
