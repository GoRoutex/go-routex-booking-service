package vn.com.routex.hub.booking.service.infrastructure.persistence.adapter.outbox;

import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.domain.outbox.model.OutBoxEvent;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.outbox.entity.OutBoxEventEntity;

@Component
public class OutboxEventPersistenceMapper {

    public OutBoxEvent toDomain(OutBoxEventEntity entity) {
        if (entity == null) {
            return null;
        }

        return OutBoxEvent.builder()
                .id(entity.getId())
                .aggregateId(entity.getAggregateId())
                .topic(entity.getTopic())
                .eventType(entity.getEventType())
                .eventKey(entity.getEventKey())
                .payload(entity.getPayload())
                .header(entity.getHeader())
                .status(entity.getStatus())
                .retryCount(entity.getRetryCount())
                .availableAt(entity.getAvailableAt())
                .processedAt(entity.getProcessedAt())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public OutBoxEventEntity toEntity(OutBoxEvent domain) {
        if (domain == null) {
            return null;
        }

        return OutBoxEventEntity.builder()
                .id(domain.getId())
                .aggregateId(domain.getAggregateId())
                .topic(domain.getTopic())
                .eventType(domain.getEventType())
                .eventKey(domain.getEventKey())
                .payload(domain.getPayload())
                .header(domain.getHeader())
                .status(domain.getStatus())
                .retryCount(domain.getRetryCount())
                .availableAt(domain.getAvailableAt())
                .processedAt(domain.getProcessedAt())
                .createdAt(domain.getCreatedAt())
                .createdBy(domain.getCreatedBy())
                .updatedAt(domain.getUpdatedAt())
                .updatedBy(domain.getUpdatedBy())
                .build();
    }
}
