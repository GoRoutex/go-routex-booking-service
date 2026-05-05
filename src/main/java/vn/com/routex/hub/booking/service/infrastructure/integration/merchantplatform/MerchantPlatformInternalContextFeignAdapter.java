package vn.com.routex.hub.booking.service.infrastructure.integration.merchantplatform;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.application.command.common.RequestContext;
import vn.com.routex.hub.booking.service.domain.tripcontext.model.TripBookingContext;
import vn.com.routex.hub.booking.service.domain.tripcontext.port.TripBookingContextQueryPort;
import vn.com.routex.hub.booking.service.domain.vehicle.model.VehicleSeatBlueprint;
import vn.com.routex.hub.booking.service.domain.vehicle.port.VehicleSeatBlueprintQueryPort;
import vn.com.routex.hub.booking.service.infrastructure.integration.merchantplatform.client.MerchantPlatformInternalContextFeignClient;
import vn.com.routex.hub.booking.service.infrastructure.integration.merchantplatform.dto.FetchTripBookingContextClientResponse;
import vn.com.routex.hub.booking.service.infrastructure.integration.merchantplatform.dto.FetchVehicleSeatBlueprintClientResponse;
import vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant;
import vn.com.routex.hub.booking.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.booking.service.infrastructure.persistence.exception.CustomFeignException;
import vn.com.routex.hub.booking.service.infrastructure.persistence.utils.ExceptionUtils;
import vn.com.routex.hub.booking.service.interfaces.models.base.BaseResponse;

@Component
@RequiredArgsConstructor
public class MerchantPlatformInternalContextFeignAdapter implements TripBookingContextQueryPort, VehicleSeatBlueprintQueryPort {

    private final MerchantPlatformInternalContextFeignClient merchantPlatformInternalContextFeignClient;

    @Override
    public TripBookingContext fetchByTripId(String tripId, RequestContext context) {
        FetchTripBookingContextClientResponse response = execute(
                context,
                () -> merchantPlatformInternalContextFeignClient.fetchTripBookingContext(tripId),
                "trip booking context"
        );
        FetchTripBookingContextClientResponse.FetchTripBookingContextClientResponseData data =
                requireData(response, context, "trip booking context");

        return TripBookingContext.builder()
                .tripId(data.getTripId())
                .routeId(data.getRouteId())
                .merchantId(data.getMerchantId())
                .vehicleId(data.getVehicleId())
                .ticketPrice(data.getTicketPrice())
                .pickupBranch(data.getPickupBranch())
                .originName(data.getOriginName())
                .destinationName(data.getDestinationName())
                .routeStatus(data.getRouteStatus())
                .tripStatus(data.getTripStatus())
                .build();
    }

    @Override
    public VehicleSeatBlueprint fetchByVehicleId(String vehicleId, RequestContext context) {
        FetchVehicleSeatBlueprintClientResponse response = execute(
                context,
                () -> merchantPlatformInternalContextFeignClient.fetchVehicleSeatBlueprint(vehicleId),
                "vehicle seat blueprint"
        );
        FetchVehicleSeatBlueprintClientResponse.FetchVehicleSeatBlueprintClientResponseData data =
                requireData(response, context, "vehicle seat blueprint");

        return VehicleSeatBlueprint.builder()
                .vehicleId(data.getVehicleId())
                .merchantId(data.getMerchantId())
                .templateId(data.getTemplateId())
                .seatCapacity(data.getSeatCapacity())
                .hasFloor(data.isHasFloor())
                .vehicleStatus(data.getVehicleStatus())
                .seats(data.getSeats().stream()
                        .map(seat -> VehicleSeatBlueprint.SeatBlueprintItem.builder()
                                .id(seat.getId())
                                .seatCode(seat.getSeatCode())
                                .floor(seat.getFloor())
                                .rowNo(seat.getRowNo())
                                .columnNo(seat.getColumnNo())
                                .build())
                        .toList())
                .build();
    }

    private <T> T requireData(BaseResponse<T> response, RequestContext context, String target) {
        if (response == null) {
            throw new BusinessException(
                    context.requestId(),
                    context.requestDateTime(),
                    context.channel(),
                    ExceptionUtils.buildResultResponse(
                            ErrorConstant.SYSTEM_ERROR,
                            String.format("Internal API returned empty %s response", target)
                    )
            );
        }

        if (response.getResult() != null
                && response.getResult().getResponseCode() != null
                && !ErrorConstant.SUCCESS_CODE.equals(response.getResult().getResponseCode())) {
            throw new BusinessException(context.requestId(), context.requestDateTime(), context.channel(), response.getResult());
        }

        if (response.getData() == null) {
            throw new BusinessException(
                    context.requestId(),
                    context.requestDateTime(),
                    context.channel(),
                    ExceptionUtils.buildResultResponse(
                            ErrorConstant.SYSTEM_ERROR,
                            String.format("Internal API returned empty %s response", target)
                    )
            );
        }

        return response.getData();
    }

    private <T> T execute(RequestContext context, InternalApiCall<T> action, String target) {
        try {
            return action.call();
        } catch (BusinessException ex) {
            throw ex;
        } catch (CustomFeignException ex) {
            throw new BusinessException(context.requestId(), context.requestDateTime(), context.channel(), ex.getResult());
        } catch (RuntimeException ex) {
            throw new BusinessException(
                    context.requestId(),
                    context.requestDateTime(),
                    context.channel(),
                    ExceptionUtils.buildResultResponse(
                            ErrorConstant.SYSTEM_ERROR,
                            String.format("Failed to call internal API for %s: %s", target, ex.getMessage())
                    )
            );
        }
    }

    @FunctionalInterface
    private interface InternalApiCall<T> {
        T call();
    }
}
