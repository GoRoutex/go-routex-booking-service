package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.outbox.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import vn.com.routex.hub.booking.service.domain.outbox.OutBoxEventStatus;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.entity.AbstractAuditingEntity;

import java.time.OffsetDateTime;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "OUTBOX_EVENT")
public class OutBoxEventEntity extends AbstractAuditingEntity {

    @Id
    private String id;

    @Column(name = "AGGREGATE_ID", nullable = false, length = 64)
    private String aggregateId;

    @Column(name = "TOPIC", nullable = false)
    private String topic;

    @Column(name = "EVENT_TYPE", nullable = false, length = 100)
    private String eventType;

    @Column(name = "EVENT_KEY", nullable = false, length = 100)
    private String eventKey;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "PAYLOAD", columnDefinition = "jsonb", nullable = false)
    private Map<String, Object> payload;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "HEADER", columnDefinition = "jsonb", nullable = false)
    private Map<String, Object> header;

    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private OutBoxEventStatus status;

    @Column(name = "RETRY_COUNT", nullable = false)
    private Integer retryCount;

    @Column(name = "AVAILABLE_AT", nullable = false)
    private OffsetDateTime availableAt;

    @Column(name = "PROCESSED_AT", nullable = false)
    private OffsetDateTime processedAt;
}
