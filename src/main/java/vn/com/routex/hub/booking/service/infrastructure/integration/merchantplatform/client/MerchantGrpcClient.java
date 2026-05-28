package vn.com.routex.hub.booking.service.infrastructure.integration.merchantplatform.client;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.grpc.MerchantGrpcServiceGrpc;
import vn.com.routex.hub.grpc.RequestContext;
import vn.com.routex.hub.grpc.TripBookingContextRequest;
import vn.com.routex.hub.grpc.TripBookingContextResponse;
import vn.com.routex.hub.grpc.VehicleSeatBlueprintRequest;
import vn.com.routex.hub.grpc.VehicleSeatBlueprintResponse;

@Service
public class MerchantGrpcClient {

    @GrpcClient("merchantService")
    private MerchantGrpcServiceGrpc.MerchantGrpcServiceBlockingStub merchantServiceStub;

    public TripBookingContextResponse getTripBookingContext(String tripId, String requestId, String requestDateTime, String channel) {
        TripBookingContextRequest request = TripBookingContextRequest.newBuilder()
                .setTripId(tripId)
                .setContext(RequestContext.newBuilder()
                        .setRequestId(requestId != null ? requestId : "")
                        .setRequestDateTime(requestDateTime != null ? requestDateTime : "")
                        .setChannel(channel != null ? channel : "")
                        .build())
                .build();
        return merchantServiceStub.getTripBookingContext(request);
    }

    public VehicleSeatBlueprintResponse getVehicleSeatBlueprint(String vehicleId, String requestId, String requestDateTime, String channel) {
        VehicleSeatBlueprintRequest request = VehicleSeatBlueprintRequest.newBuilder()
                .setVehicleId(vehicleId)
                .setContext(RequestContext.newBuilder()
                        .setRequestId(requestId != null ? requestId : "")
                        .setRequestDateTime(requestDateTime != null ? requestDateTime : "")
                        .setChannel(channel != null ? channel : "")
                        .build())
                .build();
        return merchantServiceStub.getVehicleSeatBlueprint(request);
    }
}

