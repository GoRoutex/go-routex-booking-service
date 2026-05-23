package vn.com.routex.hub.booking.service.infrastructure.integration.merchantplatform;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.application.command.common.RequestContext;
import vn.com.routex.hub.booking.service.domain.seat.SeatFloor;
import vn.com.routex.hub.booking.service.domain.tripcontext.model.TripBookingContext;
import vn.com.routex.hub.booking.service.domain.tripcontext.port.TripBookingContextQueryPort;
import vn.com.routex.hub.booking.service.domain.vehicle.VehicleStatus;
import vn.com.routex.hub.booking.service.domain.vehicle.model.VehicleSeatBlueprint;
import vn.com.routex.hub.booking.service.domain.vehicle.port.VehicleSeatBlueprintQueryPort;
import vn.com.routex.hub.booking.service.infrastructure.integration.merchantplatform.client.MerchantGrpcClient;
import vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant;
import vn.com.routex.hub.booking.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.booking.service.infrastructure.persistence.utils.ExceptionUtils;
import vn.com.routex.hub.grpc.TripBookingContextResponse;
import vn.com.routex.hub.grpc.VehicleSeatBlueprintResponse;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class MerchantPlatformInternalContextGrpcAdapter implements TripBookingContextQueryPort, VehicleSeatBlueprintQueryPort {

    private final MerchantGrpcClient merchantGrpcClient;

    @Override
    public TripBookingContext fetchByTripId(String tripId, RequestContext context) {
        try {
            TripBookingContextResponse response = merchantGrpcClient.getTripBookingContext(
                    tripId,
                    context != null ? context.requestId() : null,
                    context != null ? context.requestDateTime() : null,
                    context != null ? context.channel() : null
            );
            return TripBookingContext.builder()
                    .tripId(response.getTripId())
                    .routeId(response.getRouteId())
                    .merchantId(response.getMerchantId())
                    .vehicleId(response.getVehicleId())
                    .ticketPrice(response.getTicketPrice().isEmpty() ? BigDecimal.ZERO : new BigDecimal(response.getTicketPrice()))
                    .originName(response.getOriginName())
                    .destinationName(response.getDestinationName())
                    .routeStatus(response.getRouteStatus().isEmpty() ? null : response.getRouteStatus())
                    .tripStatus(response.getTripStatus().isEmpty() ? null : response.getTripStatus())
                    .build();
        } catch (StatusRuntimeException ex) {
            handleGrpcException(ex, context, "trip booking context");
            return null;
        }
    }

    @Override
    public VehicleSeatBlueprint fetchByVehicleId(String vehicleId, RequestContext context) {
        try {
            VehicleSeatBlueprintResponse response = merchantGrpcClient.getVehicleSeatBlueprint(
                    vehicleId,
                    context != null ? context.requestId() : null,
                    context != null ? context.requestDateTime() : null,
                    context != null ? context.channel() : null
            );
            return VehicleSeatBlueprint.builder()
                    .vehicleId(response.getVehicleId())
                    .merchantId(response.getMerchantId())
                    .templateId(response.getTemplateId())
                    .seatCapacity(response.getSeatCapacity())
                    .hasFloor(response.getHasFloor())
                    .vehicleStatus(response.getVehicleStatus().isEmpty() ? null : VehicleStatus.valueOf(response.getVehicleStatus()))
                    .seats(response.getSeatsList().stream()
                            .map(seat -> VehicleSeatBlueprint.SeatBlueprintItem.builder()
                                    .id(seat.getId())
                                    .seatCode(seat.getSeatCode())
                                    .floor(seat.getFloor().isEmpty() ? null : SeatFloor.valueOf(seat.getFloor()))
                                    .rowNo(seat.getRowNo())
                                    .columnNo(seat.getColumnNo())
                                    .build())
                            .toList())
                    .build();
        } catch (StatusRuntimeException ex) {
            handleGrpcException(ex, context, "vehicle seat blueprint");
            return null;
        }
    }

    private void handleGrpcException(StatusRuntimeException ex, RequestContext context, String target) {
        String errorMsg = ex.getStatus().getDescription();
        if (errorMsg == null) {
            errorMsg = ex.getMessage();
        }
        if (ex.getStatus().getCode() == Status.Code.NOT_FOUND || (errorMsg != null && errorMsg.contains("not found"))) {
            throw new BusinessException(
                    context.requestId(),
                    context.requestDateTime(),
                    context.channel(),
                    ExceptionUtils.buildResultResponse(ErrorConstant.RECORD_NOT_FOUND, errorMsg)
            );
        }
        throw new BusinessException(
                context.requestId(),
                context.requestDateTime(),
                context.channel(),
                ExceptionUtils.buildResultResponse(ErrorConstant.SYSTEM_ERROR,
                        String.format("Failed to call internal gRPC for %s: %s", target, errorMsg))
        );
    }
}
