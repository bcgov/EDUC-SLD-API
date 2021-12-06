package ca.bc.gov.educ.api.sld.service;

import ca.bc.gov.educ.api.sld.constant.EventOutcome;
import ca.bc.gov.educ.api.sld.constant.EventType;
import ca.bc.gov.educ.api.sld.mappers.v1.SldDiaStudentMapper;
import ca.bc.gov.educ.api.sld.model.SldDiaStudentEntity;
import ca.bc.gov.educ.api.sld.repository.EventRepository;
import ca.bc.gov.educ.api.sld.repository.SldDiaStudentRepository;
import ca.bc.gov.educ.api.sld.struct.v1.Event;
import ca.bc.gov.educ.api.sld.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SldDiaStudentService {
  public static final String NO_RECORD_SAGA_ID_EVENT_TYPE = "no record found for the saga id and event type combination, processing";
  public static final String RECORD_FOUND_FOR_SAGA_ID_EVENT_TYPE = "record found for the saga id and event type combination, might be a duplicate or replay," +
    " just updating the db status so that it will be polled and sent back again.";
  private final SldDiaStudentRepository sldDiaStudentRepository;
  private final EventRepository eventRepository;

  public SldDiaStudentService(final SldDiaStudentRepository sldDiaStudentRepository, final EventRepository eventRepository) {
    this.sldDiaStudentRepository = sldDiaStudentRepository;
    this.eventRepository = eventRepository;
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public byte[] createDiaStudents(final List<SldDiaStudentEntity> sldDiaStudentEntities, final Event event) throws JsonProcessingException {
    val eventOpt = this.eventRepository.findByEventTypeAndSagaId(event.getEventType().toString(), event.getSagaId());
    ca.bc.gov.educ.api.sld.model.Event sldEvent;
    if (eventOpt.isPresent()) {
      log.info(RECORD_FOUND_FOR_SAGA_ID_EVENT_TYPE);
      sldEvent = eventOpt.get();
      sldEvent.setUpdateDate(LocalDateTime.now());
    } else {
      log.info(NO_RECORD_SAGA_ID_EVENT_TYPE);
      val savedEntities = this.sldDiaStudentRepository.saveAll(sldDiaStudentEntities);
      event.setEventOutcome(EventOutcome.SLD_DIA_STUDENTS_CREATED);
      event.setEventPayload(JsonUtil.getJsonStringFromObject(savedEntities.stream().map(SldDiaStudentMapper.mapper::toStructure).collect(Collectors.toList())));
      sldEvent = createEvent(event);
    }
    this.eventRepository.save(sldEvent);
    return createResponseEvent(sldEvent);
  }

  private ca.bc.gov.educ.api.sld.model.Event createEvent(Event event) {
    return ca.bc.gov.educ.api.sld.model.Event.builder()
      .createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now())
      .createUser(event.getEventType().toString()) //need to discuss what to put here.
      .updateUser(event.getEventType().toString())
      .eventPayload(event.getEventPayload())
      .eventType(event.getEventType().toString())
      .sagaId(event.getSagaId())
      .eventStatus("MESSAGE_PUBLISHED")
      .eventOutcome(event.getEventOutcome().toString())
      .replyChannel(event.getReplyTo())
      .build();
  }

  private byte[] createResponseEvent(ca.bc.gov.educ.api.sld.model.Event event) throws JsonProcessingException {
    val responseEvent = Event.builder()
      .sagaId(event.getSagaId())
      .eventType(EventType.valueOf(event.getEventType()))
      .eventOutcome(EventOutcome.valueOf(event.getEventOutcome()))
      .eventPayload(event.getEventPayload()).build();
    return JsonUtil.getJsonBytesFromObject(responseEvent);
  }
}
