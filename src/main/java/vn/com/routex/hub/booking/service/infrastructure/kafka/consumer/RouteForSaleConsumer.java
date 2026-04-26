package vn.com.routex.hub.booking.service.infrastructure.kafka.consumer;


import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.application.handler.impl.RouteEventHandler;
import vn.com.routex.hub.booking.service.controller.models.base.BaseRequest;
import vn.com.routex.hub.booking.service.infrastructure.kafka.config.KafkaEventPublisher;
import vn.com.routex.hub.booking.service.infrastructure.kafka.event.DomainEvent;
import vn.com.routex.hub.booking.service.infrastructure.kafka.event.RouteOpenForBookingEvent;
import vn.com.routex.hub.booking.service.infrastructure.kafka.event.RouteSellableEvent;
import vn.com.routex.hub.booking.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.booking.service.infrastructure.persistence.log.SystemLog;
import vn.com.routex.hub.booking.service.infrastructure.persistence.utils.ExceptionUtils;
import vn.com.routex.hub.booking.service.infrastructure.persistence.utils.JsonUtils;

import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.INVALID_DATA_ERROR_MESSAGE;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.INVALID_EVENT_MESSAGE;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.INVALID_INPUT_ERROR;

@Component
@RequiredArgsConstructor
public class RouteForSaleConsumer {


    @Value("${spring.kafka.events.route-ready-for-sale}")
    private String routeReadyForSale;

    @Value("${spring.kafka.topics.notifications}")
    private String notificationTopic;

    @Value("${spring.kafka.events.notification-activities}")
    private String notificationActivitiesEvent;

    private final KafkaEventPublisher kafkaEventPublisher;
    private final RouteEventHandler routeEventHandler;
    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @KafkaListener(
            topics = "${spring.kafka.topics.routes}",
            containerFactory = "kafkaListenerContainerFactory",
            groupId = "${spring.kafka.group-id.bookings}")
    public void consume(String payload, Acknowledgment acknowledgment) {
        sLog.info("[ROUTE-FOR-SALE] Raw Payload: {}",  payload);

        DomainEvent event =
                JsonUtils.parseToKafkaObject(
                        payload,
                        new TypeReference<>() {
                        });


        sLog.info("[ROUTE-FOR-SALE] Domain Event: {}", event);


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
        RouteSellableEvent routeEvent = JsonUtils.convertValue(event.payload().get("data"), RouteSellableEvent.class);

        sLog.info("[ROUTE-EVENT] Processing event: eventName={} eventId={} aggregateId={} routeId={} vehicleId={}",
                event.eventType(),
                event.eventId(),
                event.aggregateId(),
                routeEvent.routeId(),
                routeEvent.vehicleId());

        sLog.info("[ROUTE-EVENT] Route Sellable Event: {}", routeEvent);

        try {
            validateEvent(event, context, routeEvent);
            routeEventHandler.generateRouteSeat(event, context, routeEvent);
        } catch (Exception ex) {
            sLog.error("[ROUTE-EVENT] Failed eventName={} eventId={} aggregateId={} routeId={} vehicleId={}",
                    event.eventType(),
                    event.eventId(),
                    event.aggregateId(),
                    routeEvent.routeId(),
                    routeEvent.vehicleId(),
                    ex);
            throw ex;
        }

        sLog.info("[ROUTE-EVENT] Event processed successfully: eventName={} eventId={} routeId={}", event.eventType(), event.eventId(), event.aggregateId());

        RouteOpenForBookingEvent bookingEvent = RouteOpenForBookingEvent
                .builder()
                .routeId(routeEvent.routeId())
                .vehicleId(routeEvent.vehicleId())
                .seatCount(routeEvent.seatCount())
                .creator(routeEvent.creator())
                .assignedAt(routeEvent.assignedAt())
                .build();

        kafkaEventPublisher.publish(
                context,
                notificationTopic,
                notificationActivitiesEvent,
                routeEvent.routeId(),
                bookingEvent
        );

        acknowledgment.acknowledge();
    }


    public void validateEvent(DomainEvent event, BaseRequest context, RouteSellableEvent data) {
        if (event.eventId().isBlank()
                || event.eventType().isBlank()
                || event.aggregateId().isBlank()
                || context == null
                || context.getRequestId().isBlank()
                || context.getRequestDateTime().isBlank()
                || context.getChannel().isBlank()
                || data.routeId().isBlank()
                || data.vehicleId().isBlank()) {
            throw new BusinessException(ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, String.format(INVALID_EVENT_MESSAGE, event.eventType())));
        }
    }

}
