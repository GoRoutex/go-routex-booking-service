package vn.com.routex.hub.booking.service.infrastructure.kafka.consumer;


import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.application.handler.impl.RouteEventHandler;
import vn.com.routex.hub.booking.service.infrastructure.kafka.config.KafkaEventPublisher;
import vn.com.routex.hub.booking.service.infrastructure.kafka.event.RouteOpenForBookingEvent;
import vn.com.routex.hub.booking.service.infrastructure.kafka.event.RouteSellableEvent;
import vn.com.routex.hub.booking.service.infrastructure.kafka.model.KafkaEventMessage;
import vn.com.routex.hub.booking.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.booking.service.infrastructure.persistence.log.SystemLog;
import vn.com.routex.hub.booking.service.infrastructure.persistence.utils.ExceptionUtils;
import vn.com.routex.hub.booking.service.infrastructure.persistence.utils.JsonUtils;
import vn.com.routex.hub.booking.service.controller.models.base.BaseRequest;

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
            groupId = "${spring.kafka.group-id.bookings}")
    public void consume(String payload) {

        KafkaEventMessage<RouteSellableEvent> event =
                JsonUtils.parseToKafkaObject(
                        payload,
                        new TypeReference<>() {
                        });

        if (event == null || event.data() == null) {
            throw new BusinessException(ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_DATA_ERROR_MESSAGE));
        }

        if (!routeReadyForSale.equals(event.eventName())) {
            sLog.info("Ignore event {}", event.eventName());
            return;
        }

        sLog.info("[ROUTE-EVENT] Processing event: eventName={} eventId={} aggregateId={} routeId={} vehicleId={}",
                event.eventName(),
                event.eventId(),
                event.aggregateId(),
                event.data().routeId(),
                event.data().vehicleId());

        validateEvent(event);

        routeEventHandler.generateRouteSeat(event);

        sLog.info("[ROUTE-EVENT] Event processed successfully: eventName={} eventId={} routeId={}", event.eventName(), event.eventId(), event.aggregateId());

        RouteOpenForBookingEvent bookingEvent = RouteOpenForBookingEvent
                .builder()
                .routeId(event.data().routeId())
                .vehicleId(event.data().vehicleId())
                .seatCount(event.data().seatCount())
                .creator(event.data().creator())
                .assignedAt(event.data().assignedAt())
                .build();

        kafkaEventPublisher.publish(
                new BaseRequest(event.requestId(), event.requestDateTime(), event.channel()),
                notificationTopic,
                notificationActivitiesEvent,
                event.data().routeId(),
                bookingEvent
        );
    }

    private void validateEvent(KafkaEventMessage<RouteSellableEvent> event) {
        RouteSellableEvent data = event.data();

        if (event.eventId().isBlank()
                || event.eventName().isBlank()
                || event.aggregateId().isBlank()
                || data.routeId().isBlank()
                || data.vehicleId().isBlank()) {
            throw new BusinessException(event.requestId(), event.requestDateTime(), event.channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, String.format(INVALID_EVENT_MESSAGE, event.eventName())));
        }
    }
}
