package ca.bc.gov.educ.api.sld.schedulers;

import ca.bc.gov.educ.api.sld.controller.v1.BaseSLDAPITest;
import ca.bc.gov.educ.api.sld.model.Event;
import ca.bc.gov.educ.api.sld.repository.EventRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


public class PurgeOldRecordsSchedulerTest extends BaseSLDAPITest {

  @Autowired
  EventRepository eventRepository;

  @Autowired
  PurgeOldRecordsScheduler purgeOldRecordsScheduler;


  @Test
  public void testPurgeOldRecords_givenOldRecordsPresent_shouldBeDeleted() {
    final var payload = " {\n" +
        "    \"createUser\": \"test\",\n" +
        "    \"updateUser\": \"test\",\n" +
        "    \"legalFirstName\": \"Jack\"\n" +
        "  }";

    final var yesterday = LocalDateTime.now().minusDays(1);

    this.eventRepository.save(this.getEvent(payload, LocalDateTime.now()));

    this.eventRepository.save(this.getEvent(payload, yesterday));

    this.purgeOldRecordsScheduler.setEventRecordStaleInDays(1);
    this.purgeOldRecordsScheduler.purgeOldRecords();

    final var servicesEvents = this.eventRepository.findAll();
    assertThat(servicesEvents).hasSize(1);
  }


  private Event getEvent(final String payload, final LocalDateTime createDateTime) {
    return Event
      .builder()
      .eventPayloadBytes(payload.getBytes())
      .eventStatus("MESSAGE_PUBLISHED")
      .eventType("UPDATE_SLD_STUDENTS")
      .sagaId(UUID.randomUUID())
      .eventOutcome("SLD_STUDENT_UPDATED")
      .replyChannel("TEST_CHANNEL")
      .createDate(createDateTime)
      .createUser("SLD_API")
      .updateUser("SLD_API")
      .updateDate(createDateTime)
      .build();
  }
}
