package vn.com.routex.hub.booking.service.infrastructure.persistence.adapter.outbox;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.domain.outbox.OutBoxEventStatus;
import vn.com.routex.hub.booking.service.domain.outbox.model.OutBoxEvent;
import vn.com.routex.hub.booking.service.domain.outbox.port.OutBoxEventRepositoryPort;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.outbox.repository.OutBoxEventRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OutBoxEventRepositoryAdapter implements OutBoxEventRepositoryPort {

    private final OutBoxEventRepository outBoxEventRepository;
    private final OutboxEventPersistenceMapper outboxEventPersistenceMapper;

    @Override
    public void save(OutBoxEvent outboxEvent) {
        outBoxEventRepository.save(outboxEventPersistenceMapper.toEntity(outboxEvent));
    }

    @Override
    public void markAsProcessed(List<String> processedIds, OffsetDateTime now) {
        outBoxEventRepository.markAsProcessed(processedIds, OutBoxEventStatus.PROCESSED, now, now);
    }

    @Override
    public void markAsFailed(List<String> failedIds, OffsetDateTime now) {
        outBoxEventRepository.markAsFailed(failedIds, OutBoxEventStatus.FAILED, now);
    }

    @Override
    public List<OutBoxEvent> lockPendingBatch(int limit) {
        return outBoxEventRepository.lockPendingBatch(limit).stream()
                .map(outboxEventPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }
}
