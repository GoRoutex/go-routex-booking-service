package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.idempotency.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "PROCESSED_EVENT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProcessedEventEntity {

    @Id
    private String eventId;

    @Column(name = "CONSUMER_NAME", nullable = false)
    private String consumerName;

    @Column(name = "PROCESSED_AT", nullable = false)
    private OffsetDateTime processedAt;
}
