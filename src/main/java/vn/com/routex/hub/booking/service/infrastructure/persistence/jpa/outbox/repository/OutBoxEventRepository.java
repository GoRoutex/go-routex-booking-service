package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.outbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.routex.hub.booking.service.domain.outbox.OutBoxEventStatus;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.outbox.entity.OutBoxEventEntity;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface OutBoxEventRepository extends JpaRepository<OutBoxEventEntity, Integer> {

    @Query(value = """
                SELECT * FROM outbox_event
                WHERE status = 'PENDING'
                ORDER by id
                FOR UPDATE SKIP LOCKED
                LIMIT :limit
                """, nativeQuery = true)
    List<OutBoxEventEntity> lockPendingBatch(@Param("limit") int limit);

    List<OutBoxEventEntity> findAllByIdIn(List<String> processedIds);


    @Modifying
    @Query(value = """
                    UPDATE OutBoxEventEntity ev
                    SET ev.status = :outBoxEventStatus,
                        ev.processedAt = :processedTime,
                        ev.updatedAt = :updatedTime
                    WHERE ev.id in :processedIds
                    """)
    void markAsProcessed(@Param("processedIds") List<String> processedIds,
                         @Param("outBoxEventStatus") OutBoxEventStatus outBoxEventStatus,
                         @Param("processedTime") OffsetDateTime processedTime,
                         @Param("updatedTime") OffsetDateTime updatedTime);

    @Modifying
    @Query(value = """
                    UPDATE OutBoxEventEntity ev
                    SET ev.status = :outBoxEventStatus,
                        ev.updatedAt = :updatedTime
                    WHERE ev.id in :failedIds
                    """)
    void markAsFailed(@Param("failedIds") List<String> failedIds,
                      @Param("outBoxEventStatus") OutBoxEventStatus outBoxEventStatus,
                      @Param("updatedTime") OffsetDateTime updatedTime);
}
