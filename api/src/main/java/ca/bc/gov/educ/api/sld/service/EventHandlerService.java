package ca.bc.gov.educ.api.sld.service;

import ca.bc.gov.educ.api.sld.constant.EntityName;
import ca.bc.gov.educ.api.sld.constant.EventOutcome;
import ca.bc.gov.educ.api.sld.mappers.v1.SldDiaStudentMapper;
import ca.bc.gov.educ.api.sld.mappers.v1.SldStudentMapper;
import ca.bc.gov.educ.api.sld.mappers.v1.SldStudentProgramMapper;
import ca.bc.gov.educ.api.sld.model.SldDiaStudentEntity;
import ca.bc.gov.educ.api.sld.model.SldStudentEntity;
import ca.bc.gov.educ.api.sld.model.SldStudentProgramEntity;
import ca.bc.gov.educ.api.sld.struct.v1.Event;
import ca.bc.gov.educ.api.sld.struct.v1.SldUpdateDiaStudentsEvent;
import ca.bc.gov.educ.api.sld.struct.v1.SldUpdateStudentProgramsEvent;
import ca.bc.gov.educ.api.sld.struct.v1.SldUpdateStudentsEvent;
import ca.bc.gov.educ.api.sld.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The type Event handler service.
 */
@Service
@Slf4j
@SuppressWarnings("java:S3864")
public class EventHandlerService {

  /**
   * The constant EVENT_PAYLOAD.
   */
  public static final String EVENT_PAYLOAD = "event is :: {}";

  private static final SldStudentMapper sldStudentMapper = SldStudentMapper.mapper;
  private static final SldDiaStudentMapper sldDiaStudentMapper = SldDiaStudentMapper.mapper;
  private static final SldStudentProgramMapper sldStudentProgramMapper = SldStudentProgramMapper.mapper;

  private final Map<EntityName, SldService<?>> sldServiceMap;

  /**
   * Instantiates a new Event handler service.
   *
   * @param sldServices the service classes
   */
  @Autowired
  public EventHandlerService(final List<SldService<?>> sldServices) {
    this.sldServiceMap = sldServices.stream().collect(Collectors.toConcurrentMap(SldService::getEntityName, sldService -> sldService));
  }

  /**
   * Handle update sld students event.
   *
   * @param event the event
   * @return the byte [ ]
   * @throws JsonProcessingException the json processing exception
   */
  public byte[] handleUpdateStudentsEvent(final Event event) throws JsonProcessingException {
    log.trace(EVENT_PAYLOAD, event);
    final var updateEvent = JsonUtil.getJsonObjectFromString(SldUpdateStudentsEvent.class, event.getEventPayload());
    final SldService<SldStudentEntity> service = (SldService<SldStudentEntity>) this.sldServiceMap.get(EntityName.STUDENT);
    final var students = service.update(updateEvent.getPen(), sldStudentMapper.toModel(updateEvent.getSldStudent()));
    event.setEventPayload(JsonUtil.getJsonStringFromObject(students.stream().map(sldStudentMapper::toStructure).collect(Collectors.toList())));// need to convert to structure MANDATORY otherwise jackson will break.
    event.setEventOutcome(EventOutcome.SLD_STUDENT_UPDATED);

    return this.createResponseEvent(event);
  }

  /**
   * Handle restore sld students event.
   *
   * @param event the event
   * @return the byte [ ]
   * @throws JsonProcessingException the json processing exception
   */
  public byte[] handleRestoreStudentsEvent(final Event event) throws JsonProcessingException {
    log.trace(EVENT_PAYLOAD, event);
    final var updateEvent = JsonUtil.getJsonObjectFromString(SldUpdateStudentsEvent.class, event.getEventPayload());
    final SldService<SldStudentEntity> service = (SldService<SldStudentEntity>) this.sldServiceMap.get(EntityName.STUDENT);
    final var students = service.restore(updateEvent.getPen(), sldStudentMapper.toModel(updateEvent.getSldStudent()));
    event.setEventPayload(JsonUtil.getJsonStringFromObject(students.stream().map(sldStudentMapper::toStructure).collect(Collectors.toList())));// need to convert to structure MANDATORY otherwise jackson will break.
    event.setEventOutcome(EventOutcome.SLD_STUDENT_RESTORED);

    return this.createResponseEvent(event);
  }

  /**
   * Handle update sld dia students event.
   *
   * @param event the event
   * @return the byte [ ]
   * @throws JsonProcessingException the json processing exception
   */
  public byte[] handleUpdateDiaStudentsEvent(final Event event) throws JsonProcessingException {
    log.trace(EVENT_PAYLOAD, event);
    final var updateEvent = JsonUtil.getJsonObjectFromString(SldUpdateDiaStudentsEvent.class, event.getEventPayload());
    final SldService<SldDiaStudentEntity> service = (SldService<SldDiaStudentEntity>) this.sldServiceMap.get(EntityName.DIA_STUDENT);
    final var students = service.update(updateEvent.getPen(), sldDiaStudentMapper.toModel(updateEvent.getSldDiaStudent()));
    event.setEventPayload(JsonUtil.getJsonStringFromObject(students.stream().map(sldDiaStudentMapper::toStructure).collect(Collectors.toList())));// need to convert to structure MANDATORY otherwise jackson will break.
    event.setEventOutcome(EventOutcome.SLD_DIA_STUDENT_UPDATED);

    return this.createResponseEvent(event);
  }

  /**
   * Handle update sld student programs event.
   *
   * @param event the event
   * @return the byte [ ]
   * @throws JsonProcessingException the json processing exception
   */
  public byte[] handleUpdateStudentProgramsEvent(final Event event) throws JsonProcessingException {
    log.trace(EVENT_PAYLOAD, event);
    final var updateEvent = JsonUtil.getJsonObjectFromString(SldUpdateStudentProgramsEvent.class, event.getEventPayload());
    final SldService<SldStudentProgramEntity> service = (SldService<SldStudentProgramEntity>) this.sldServiceMap.get(EntityName.STUDENT_PROGRAMS);
    final var students = service.update(updateEvent.getPen(), sldStudentProgramMapper.toModel(updateEvent.getSldStudentProgram()));
    event.setEventPayload(JsonUtil.getJsonStringFromObject(students.stream().map(sldStudentProgramMapper::toStructure).collect(Collectors.toList())));// need to convert to structure MANDATORY otherwise jackson will break.
    event.setEventOutcome(EventOutcome.SLD_STUDENT_PROGRAM_UPDATED);

    return this.createResponseEvent(event);
  }

  /**
   * Handle restore sld student programs event.
   *
   * @param event the event
   * @return the byte [ ]
   * @throws JsonProcessingException the json processing exception
   */
  public byte[] handleRestoreStudentProgramsEvent(final Event event) throws JsonProcessingException {
    log.trace(EVENT_PAYLOAD, event);
    final var updateEvent = JsonUtil.getJsonObjectFromString(SldUpdateStudentProgramsEvent.class, event.getEventPayload());
    final SldService<SldStudentProgramEntity> service = (SldService<SldStudentProgramEntity>) this.sldServiceMap.get(EntityName.STUDENT_PROGRAMS);
    final var students = service.restore(updateEvent.getPen(), sldStudentProgramMapper.toModel(updateEvent.getSldStudentProgram()));
    event.setEventPayload(JsonUtil.getJsonStringFromObject(students.stream().map(sldStudentProgramMapper::toStructure).collect(Collectors.toList())));// need to convert to structure MANDATORY otherwise jackson will break.
    event.setEventOutcome(EventOutcome.SLD_STUDENT_PROGRAM_RESTORED);

    return this.createResponseEvent(event);
  }

  private byte[] createResponseEvent(final Event event) throws JsonProcessingException {
    val responseEvent = Event.builder()
      .sagaId(event.getSagaId())
      .eventType(event.getEventType())
      .eventOutcome(event.getEventOutcome())
      .eventPayload(event.getEventPayload()).build();
    return JsonUtil.getJsonBytesFromObject(responseEvent);
  }
}
