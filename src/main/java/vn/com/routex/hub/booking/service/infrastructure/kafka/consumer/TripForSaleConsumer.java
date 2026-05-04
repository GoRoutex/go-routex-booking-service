package vn.com.routex.hub.booking.service.infrastructure.kafka.consumer;


import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.hub.booking.service.application.handler.impl.TripEventHandler;
import vn.com.routex.hub.booking.service.infrastructure.kafka.config.KafkaEventPublisher;
import vn.com.routex.hub.booking.service.infrastructure.kafka.event.DomainEvent;
import vn.com.routex.hub.booking.service.infrastructure.kafka.event.TripOpenForBookingEvent;
import vn.com.routex.hub.booking.service.infrastructure.kafka.event.TripSellableEvent;
import vn.com.routex.hub.booking.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.booking.service.infrastructure.persistence.utils.ExceptionUtils;
import vn.com.routex.hub.booking.service.infrastructure.persistence.utils.JsonUtils;
import vn.com.routex.hub.booking.service.interfaces.models.base.BaseRequest;

import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.INVALID_DATA_ERROR_MESSAGE;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.INVALID_EVENT_MESSAGE;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.INVALID_INPUT_ERROR;

@Component
@RequiredArgsConstructor
public class TripForSaleConsumer {


    @Value("${spring.kafka.events.trip-ready-for-sale}")
    private String routeReadyForSale;

    @Value("${spring.kafka.topics.notifications}")
    private String notificationTopic;

    @Value("${spring.kafka.events.notification-activities}")
    private String notificationActivitiesEvent;

    private final KafkaEventPublisher kafkaEventPublisher;
    private final TripEventHandler tripEventHandler;
    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @KafkaListener(
            topics = "${spring.kafka.topics.trips}",
            containerFactory = "kafkaListenerContainerFactory",
            groupId = "${spring.kafka.group-id.trips}")
    public void consume(String payload, Acknowledgment acknowledgment) {
        sLog.info("[TRIP-FOR-SALE] Raw Payload: {}",  payload);

        DomainEvent event =
                JsonUtils.parseToKafkaObject(
                        payload,
                        new TypeReference<>() {
                        });


        sLog.info("[TRIP-FOR-SALE] Domain Event: {}", event);


        if (event == null
                || event.header() == null
                || event.payload() == null
                || event.header().get("context") == null
                || event.payload().get("data") == null) {
            throw new BusinessException(ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_DATA_ERROR_MESSAGE));
        }

        if (!routeReadyForSale.equals(event.eventType())) {
            sLog.info("Ignore event {}", event.eventType());
            acknowledgment.acknowledge();
            return;
        }

        BaseRequest context = JsonUtils.convertValue(event.header().get("context"), BaseRequest.class);
        TripSellableEvent routeEvent = JsonUtils.convertValue(event.payload().get("data"), TripSellableEvent.class);

        sLog.info("[TRIP-EVENT] Processing event: eventName={} eventId={} aggregateId={} tripId={} vehicleId={}",
                event.eventType(),
                event.eventId(),
                event.aggregateId(),
                routeEvent.tripId(),
                routeEvent.vehicleId());

        sLog.info("[ROUTE-EVENT] Route Sellable Event: {}", routeEvent);

        try {
            validateEvent(event, context, routeEvent);
            tripEventHandler.generateRouteSeat(event, context, routeEvent);
        } catch (Exception ex) {
            sLog.error("[ROUTE-EVENT] Failed eventName={} eventId={} aggregateId={} routeId={} vehicleId={}",
                    event.eventType(),
                    event.eventId(),
                    event.aggregateId(),
                    routeEvent.tripId(),
                    routeEvent.vehicleId(),
                    ex);
            throw ex;
        }

        sLog.info("[ROUTE-EVENT] Event processed successfully: eventName={} eventId={} routeId={}", event.eventType(), event.eventId(), event.aggregateId());

        TripOpenForBookingEvent bookingEvent = TripOpenForBookingEvent
                .builder()
                .tripId(routeEvent.tripId())
                .vehicleId(routeEvent.vehicleId())
                .seatCount(routeEvent.seatCount())
                .creator(routeEvent.creator())
                .assignedAt(routeEvent.assignedAt())
                .build();

        kafkaEventPublisher.publish(
                context,
                notificationTopic,
                notificationActivitiesEvent,
                routeEvent.tripId(),
                bookingEvent
        );

        acknowledgment.acknowledge();
    }


    public void validateEvent(DomainEvent event, BaseRequest context, TripSellableEvent data) {
        if (event.eventId().isBlank()
                || event.eventType().isBlank()
                || event.aggregateId().isBlank()
                || context == null
                || context.getRequestId().isBlank()
                || context.getRequestDateTime().isBlank()
                || context.getChannel().isBlank()
                || data.tripId().isBlank()
                || data.vehicleId().isBlank()) {
            throw new BusinessException(ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, String.format(INVALID_EVENT_MESSAGE, event.eventType())));
        }
    }

}
