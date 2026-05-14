package vn.com.routex.hub.booking.service.application.services;

import vn.com.routex.hub.booking.service.interfaces.models.base.BaseRequest;

public interface OutBoxService {
    void generateEvent(String aggregateId, String topic, String eventName, String eventKey, Object payload, BaseRequest context);
}
