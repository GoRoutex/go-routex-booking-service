package vn.com.routex.hub.booking.service.domain.outbox.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.booking.service.domain.auditing.AbstractAuditingEntity;
import vn.com.routex.hub.booking.service.domain.outbox.OutBoxEventStatus;

import java.time.OffsetDateTime;
import java.util.Map;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class OutBoxEvent extends AbstractAuditingEntity {
    private String id;
    private String topic;
    private String aggregateId;
    private String eventType;
    private String eventKey;
    private Map<String, Object> payload;
    private Map<String, Object> header;
    private OutBoxEventStatus status;
    private Integer retryCount;
    private OffsetDateTime availableAt;
    private OffsetDateTime processedAt;
}
