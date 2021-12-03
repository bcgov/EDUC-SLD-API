package ca.bc.gov.educ.api.sld.repository;

import ca.bc.gov.educ.api.sld.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
  Optional<Event> findByEventTypeAndSagaId(String eventType, UUID sagaId);
}
