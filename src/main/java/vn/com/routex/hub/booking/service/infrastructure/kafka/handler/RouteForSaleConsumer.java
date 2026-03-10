package vn.com.routex.hub.booking.service.infrastructure.kafka.handler;


import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.domain.route.Route;
import vn.com.routex.hub.booking.service.infrastructure.kafka.event.RouteReadyForSaleEvent;
import vn.com.routex.hub.booking.service.infrastructure.kafka.model.KafkaEventMessage;
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


    private final SystemLog sLog = SystemLog.getLogger(this.getClass());
    @KafkaListener(
            topics = "${spring.kafka.topics.routes}",
            groupId = "booking-service")
    @Transactional
    public void consume(String payload) {

        KafkaEventMessage<RouteReadyForSaleEvent> event =
                JsonUtils.parseToKafkaObject(
                        payload,
                        new TypeReference<>() {
                        });


        if(!routeReadyForSale.equals(event.eventName())) {
            return;
        }

        sLog.info("[MESSAGE] Consumed message successfully");
        validateEvent(event);

    }

    private void validateEvent(KafkaEventMessage<RouteReadyForSaleEvent> event) {
        if(event == null || event.data() == null) {
            throw new BusinessException(event.requestId(), event.requestDateTime(), event.channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_DATA_ERROR_MESSAGE));
        }

        RouteReadyForSaleEvent data = event.data();

        if(event.eventId().isBlank()
        || event.eventName().isBlank()
        || event.aggregateId().isBlank()
        || data.routeId().isBlank()
        || data.vehicleId().isBlank()) {
            throw new BusinessException(event.requestId(), event.requestDateTime(), event.channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, String.format(INVALID_EVENT_MESSAGE, event.eventName())));
        }
    }
}
