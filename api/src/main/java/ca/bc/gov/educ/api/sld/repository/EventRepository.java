package ca.bc.gov.educ.api.sld.repository;

import ca.bc.gov.educ.api.sld.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
  Optional<Event> findByEventTypeAndSagaId(String eventType, UUID sagaId);

  @Transactional
  @Modifying
  @Query("delete from Event where createDate <= :createDate")
  void deleteByCreateDateBefore(LocalDateTime createDate);
}
