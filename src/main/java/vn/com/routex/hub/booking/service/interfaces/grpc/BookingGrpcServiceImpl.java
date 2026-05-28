package vn.com.routex.hub.booking.service.interfaces.grpc;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.hub.booking.service.application.command.common.RequestContext;
import vn.com.routex.hub.booking.service.application.services.BookingPaymentQueryService;
import vn.com.routex.hub.booking.service.domain.booking.model.Booking;
import vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant;
import vn.com.routex.hub.booking.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.grpc.BookingGrpcServiceGrpc;
import vn.com.routex.hub.grpc.FetchBookingPaymentContextRequest;
import vn.com.routex.hub.grpc.FetchBookingPaymentContextResponse;
import vn.com.routex.hub.grpc.HoldSeatProtoRequest;
import vn.com.routex.hub.grpc.HoldSeatProtoResponse;
import vn.com.routex.hub.grpc.HoldSeatItemProtoResult;
import vn.com.routex.hub.booking.service.application.services.HoldSeatService;
import vn.com.routex.hub.booking.service.application.command.seat.HoldSeatCommand;
import vn.com.routex.hub.booking.service.application.command.seat.HoldSeatResult;

@GrpcService
@RequiredArgsConstructor
public class BookingGrpcServiceImpl extends BookingGrpcServiceGrpc.BookingGrpcServiceImplBase {

    private final BookingPaymentQueryService bookingPaymentQueryService;
    private final HoldSeatService holdSeatService;
    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @Override
    public void fetchBookingPaymentContext(FetchBookingPaymentContextRequest request,
                                           StreamObserver<FetchBookingPaymentContextResponse> responseObserver) {
        sLog.info("[GRPC] Received fetchBookingPaymentContext request for bookingCode: {}", request.getBookingCode());
        try {
            RequestContext context = RequestContext.builder()
                    .requestId(request.getContext().getRequestId())
                    .requestDateTime(request.getContext().getRequestDateTime())
                    .channel(request.getContext().getChannel())
                    .build();

            Booking booking = bookingPaymentQueryService.getBookingPaymentContext(request.getBookingCode(), context);

            FetchBookingPaymentContextResponse response = FetchBookingPaymentContextResponse.newBuilder()
                    .setBookingId(booking.getId() != null ? booking.getId() : "")
                    .setBookingCode(booking.getBookingCode() != null ? booking.getBookingCode() : "")
                    .setTotalAmount(booking.getTotalAmount() != null ? booking.getTotalAmount().toString() : "0")
                    .setCurrency(booking.getCurrency() != null ? booking.getCurrency() : "")
                    .setBookingStatus(booking.getStatus() != null ? booking.getStatus().name() : "")
                    .setHoldUntil(booking.getHoldUntil() != null ? booking.getHoldUntil().toString() : "")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception ex) {
            sLog.error("[GRPC] Error fetching booking payment context: {}", ex.getMessage(), ex);
            handleException(ex, responseObserver);
        }
    }

    @Override
    public void holdSeat(HoldSeatProtoRequest request,
                         StreamObserver<HoldSeatProtoResponse> responseObserver) {
        sLog.info("[GRPC] Received holdSeat request for tripId: {}, seats: {}", request.getTripId(), request.getSeatNosList());
        try {
            RequestContext context = RequestContext.builder()
                    .requestId(request.getContext().getRequestId())
                    .requestDateTime(request.getContext().getRequestDateTime())
                    .channel(request.getContext().getChannel())
                    .build();

            HoldSeatCommand command = HoldSeatCommand.builder()
                    .context(context)
                    .tripId(request.getTripId())
                    .seatNos(request.getSeatNosList())
                    .holdBy(request.getHoldBy())
                    .customerName(request.getCustomerName())
                    .customerPhone(request.getCustomerPhone())
                    .customerEmail(request.getCustomerEmail())
                    .pickupType(request.getPickupType())
                    .pickupStopId(request.getPickupStopId())
                    .pickupAddress(request.getPickupAddress())
                    .dropoffType(request.getDropoffType())
                    .dropoffStopId(request.getDropoffStopId())
                    .dropoffAddress(request.getDropoffAddress())
                    .build();

            HoldSeatResult result = holdSeatService.holdSeat(command);

            String holdToken = result.seats().isEmpty() ? "" : result.seats().get(0).holdToken();

            HoldSeatProtoResponse response = HoldSeatProtoResponse.newBuilder()
                    .setBookingId(result.booking().bookingId() != null ? result.booking().bookingId() : "")
                    .setBookingCode(result.booking().bookingCode() != null ? result.booking().bookingCode() : "")
                    .setHoldUntil(result.booking().holdUntil() != null ? result.booking().holdUntil().toString() : "")
                    .setTotalAmount(result.booking().totalAmount() != null ? result.booking().totalAmount().toString() : "0")
                    .setHoldToken(holdToken)
                    .addAllSeats(result.seats().stream()
                            .map(seat -> HoldSeatItemProtoResult.newBuilder()
                                    .setTripId(seat.tripId() != null ? seat.tripId() : "")
                                    .setSeatNo(seat.seatNo() != null ? seat.seatNo() : "")
                                    .setStatus(seat.status() != null ? seat.status() : "")
                                    .build())
                            .toList())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception ex) {
            sLog.error("[GRPC] Error holding seats: {}", ex.getMessage(), ex);
            handleException(ex, responseObserver);
        }
    }

    private void handleException(Throwable ex, StreamObserver<?> responseObserver) {
        if (ex instanceof BusinessException businessEx) {
            String code = businessEx.getResult() != null ? businessEx.getResult().getResponseCode() : "99";
            String desc = businessEx.getResult() != null ? businessEx.getResult().getDescription() : ex.getMessage();
            Status status = Status.INTERNAL;
            if (ErrorConstant.RECORD_NOT_FOUND.equals(code)) {
                status = Status.NOT_FOUND;
            }
            responseObserver.onError(status.withDescription(desc).asRuntimeException());
        } else {
            responseObserver.onError(Status.INTERNAL.withDescription(ex.getMessage()).asRuntimeException());
        }
    }
}
