package vn.com.routex.hub.booking.service.application.facade;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import vn.com.routex.hub.booking.service.interfaces.models.seat.GetAllSeatRequest;
import vn.com.routex.hub.booking.service.interfaces.models.seat.GetAllSeatResponse;
import vn.com.routex.hub.booking.service.interfaces.models.seat.HoldSeatRequest;
import vn.com.routex.hub.booking.service.interfaces.models.seat.HoldSeatResponse;

public interface RouteSeatManagementFacade {

    ResponseEntity<GetAllSeatResponse> getAllSeat(GetAllSeatRequest request);

    ResponseEntity<HoldSeatResponse> holdSeat(@Valid HoldSeatRequest request);
}
