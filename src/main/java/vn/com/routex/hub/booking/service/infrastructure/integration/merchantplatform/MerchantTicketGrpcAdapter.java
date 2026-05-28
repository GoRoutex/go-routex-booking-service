package vn.com.routex.hub.booking.service.infrastructure.integration.merchantplatform;

import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.hub.booking.service.domain.booking.model.BookingSeat;
import vn.com.routex.hub.booking.service.domain.ticket.TicketStatus;
import vn.com.routex.hub.booking.service.domain.ticket.model.Ticket;
import vn.com.routex.hub.booking.service.infrastructure.kafka.record.BookingAggregate;
import vn.com.routex.hub.booking.service.infrastructure.persistence.utils.DateTimeUtils;
import vn.com.routex.hub.grpc.MerchantGrpcServiceGrpc;
import vn.com.routex.hub.grpc.CreateTicketsRequest;
import vn.com.routex.hub.grpc.CreateTicketsResponse;
import vn.com.routex.hub.grpc.CreateTicketItem;
import vn.com.routex.hub.grpc.RequestContext;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MerchantTicketGrpcAdapter {

    @GrpcClient("merchantService")
    private MerchantGrpcServiceGrpc.MerchantGrpcServiceBlockingStub merchantServiceStub;
    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    public List<Ticket> createTickets(BookingAggregate aggregate, OffsetDateTime paidAt) {
        OffsetDateTime issuedAt = paidAt != null ? paidAt : OffsetDateTime.now();

        RequestContext grpcContext = RequestContext.newBuilder()
                .setRequestId(UUID.randomUUID().toString())
                .setRequestDateTime(DateTimeUtils.getCurrentRequestDateTime())
                .setChannel("INTERNAL")
                .build();

        CreateTicketsRequest request = CreateTicketsRequest.newBuilder()
                .setContext(grpcContext)
                .addAllData(aggregate.bookingSeats().stream()
                        .map(bookingSeat -> CreateTicketItem.newBuilder()
                                .setBookingId(aggregate.booking().getId() != null ? aggregate.booking().getId() : "")
                                .setBookingSeatId(bookingSeat.getId() != null ? bookingSeat.getId() : "")
                                .setMerchantId(aggregate.booking().getMerchantId() != null ? aggregate.booking().getMerchantId() : "")
                                .setTripId(bookingSeat.getTripId() != null ? bookingSeat.getTripId() : "")
                                .setVehicleId(aggregate.booking().getVehicleId() != null ? aggregate.booking().getVehicleId() : "")
                                .setSeatNumber(bookingSeat.getSeatNo() != null ? bookingSeat.getSeatNo() : "")
                                .setCustomerName(aggregate.booking().getCustomerName() != null ? aggregate.booking().getCustomerName() : "")
                                .setCustomerPhone(aggregate.booking().getCustomerPhone() != null ? aggregate.booking().getCustomerPhone() : "")
                                .setCustomerEmail(aggregate.booking().getCustomerEmail() != null ? aggregate.booking().getCustomerEmail() : "")
                                .setPrice(bookingSeat.getPrice() != null ? bookingSeat.getPrice().toString() : "0")
                                .setIssuedAt(issuedAt.toString())
                                .setCreator(aggregate.booking().getCreator() != null ? aggregate.booking().getCreator() : "")
                                .setPickupType(aggregate.booking().getPickupType() != null ? aggregate.booking().getPickupType() : "")
                                .setPickupStopId(aggregate.booking().getPickupStopId() != null ? aggregate.booking().getPickupStopId() : "")
                                .setPickupAddress(aggregate.booking().getPickupAddress() != null ? aggregate.booking().getPickupAddress() : "")
                                .setDropoffType(aggregate.booking().getDropoffType() != null ? aggregate.booking().getDropoffType() : "")
                                .setDropoffStopId(aggregate.booking().getDropoffStopId() != null ? aggregate.booking().getDropoffStopId() : "")
                                .setDropoffAddress(aggregate.booking().getDropoffAddress() != null ? aggregate.booking().getDropoffAddress() : "")
                                .build())
                        .toList())
                .build();

        sLog.info("[GRPC] Sending CreateTickets request: {}", request);
        try {
            CreateTicketsResponse response = merchantServiceStub.createTickets(request);
            sLog.info("[GRPC] Received CreateTickets response: {}", response);

            return response.getDataList().stream()
                    .map(item -> Ticket.builder()
                            .id(item.getTicketId())
                            .ticketCode(item.getTicketCode())
                            .bookingId(aggregate.booking().getId())
                            .bookingSeatId(item.getBookingSeatId())
                            .vehicleId(aggregate.booking().getVehicleId())
                            .tripId(aggregate.booking().getTripId())
                            .seatNumber(aggregate.bookingSeats().stream()
                                    .filter(s -> s.getId().equals(item.getBookingSeatId()))
                                    .findFirst()
                                    .map(BookingSeat::getSeatNo)
                                    .orElse(""))
                            .price(aggregate.bookingSeats().stream()
                                    .filter(s -> s.getId().equals(item.getBookingSeatId()))
                                    .findFirst()
                                    .map(BookingSeat::getPrice)
                                    .orElse(java.math.BigDecimal.ZERO))
                            .status(item.getStatus().isEmpty() ? TicketStatus.ISSUED : TicketStatus.valueOf(item.getStatus()))
                            .issuedAt(issuedAt)
                            .pickupType(aggregate.booking().getPickupType())
                            .pickupStopId(aggregate.booking().getPickupStopId())
                            .pickupAddress(aggregate.booking().getPickupAddress())
                            .dropoffType(aggregate.booking().getDropoffType())
                            .dropoffStopId(aggregate.booking().getDropoffStopId())
                            .dropoffAddress(aggregate.booking().getDropoffAddress())
                            .build())
                    .collect(Collectors.toList());
        } catch (StatusRuntimeException e) {
            sLog.error("[GRPC] Error calling merchant platform to create tickets", e);
            throw new RuntimeException("Giao dịch tạo vé không thành công qua gRPC: " + e.getStatus().getDescription(), e);
        }
    }
}
