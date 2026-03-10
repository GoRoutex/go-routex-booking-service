package vn.com.routex.hub.booking.service.infrastructure.kafka.idempotency;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

@Entity
@Table(name = "PROCESSED_EVENT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProcessedEvent {
    @Id
    private String eventId;

    @Column(name = "CONSUMER_NAME",nullable = false)
    private String consumerName;

    @Column(name = "PROCESSED_AT", nullable = false)
    private OffsetDateTime processedAt;

}
