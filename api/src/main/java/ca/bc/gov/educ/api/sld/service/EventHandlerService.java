package ca.bc.gov.educ.api.sld.service;

import ca.bc.gov.educ.api.sld.constant.EntityName;
import ca.bc.gov.educ.api.sld.constant.EventOutcome;
import ca.bc.gov.educ.api.sld.mappers.v1.*;
import ca.bc.gov.educ.api.sld.model.*;
import ca.bc.gov.educ.api.sld.struct.v1.*;
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
  private static final SldStudentProgramMapper sldStudentProgramMapper = SldStudentProgramMapper.mapper;
  private static final SldUpdateStudentsEventMapper sldUpdateStudentsEventMapper = SldUpdateStudentsEventMapper.mapper;
  private static final SldUpdateSingleStudentEventMapper sldUpdateSingleStudentEventMapper = SldUpdateSingleStudentEventMapper.mapper;
  private static final SldUpdateStudentProgramsEventMapper sldUpdateStudentProgramsEventMapper = SldUpdateStudentProgramsEventMapper.mapper;

  private final Map<EntityName, SldService<?, ?>> sldServiceMap;

  /**
   * Instantiates a new Event handler service.
   *
   * @param sldServices the service classes
   */
  @Autowired
  public EventHandlerService(final List<SldService<?, ?>> sldServices) {
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
    final SldService<SldStudentId, SldStudentEntity> service = (SldService<SldStudentId, SldStudentEntity>) this.sldServiceMap.get(EntityName.STUDENT);
    final var students = service.updateBatch(sldUpdateStudentsEventMapper.toSldStudentEntity(updateEvent), sldStudentMapper.toModel(updateEvent.getSldStudent()));
    event.setEventPayload(JsonUtil.getJsonStringFromObject(students.stream().map(sldStudentMapper::toStructure).collect(Collectors.toList())));// need to convert to structure MANDATORY otherwise jackson will break.
    event.setEventOutcome(EventOutcome.SLD_STUDENT_UPDATED);

    return this.createResponseEvent(event);
  }

  /**
   * Handle update single sld student event.
   *
   * @param event the event
   * @return the byte [ ]
   * @throws JsonProcessingException the json processing exception
   */
  public byte[] handleUpdateStudentEvent(final Event event) throws JsonProcessingException {
    log.trace(EVENT_PAYLOAD, event);
    final var updateEvent = JsonUtil.getJsonObjectFromString(SldUpdateSingleStudentEvent.class, event.getEventPayload());
    final SldService<SldStudentId, SldStudentEntity> service = (SldService<SldStudentId, SldStudentEntity>) this.sldServiceMap.get(EntityName.STUDENT);
    final var student = service.update(sldUpdateSingleStudentEventMapper.toSldStudentId(updateEvent), sldStudentMapper.toModel(updateEvent.getSldStudent()));
    if(student.isPresent()) {
      event.setEventPayload(JsonUtil.getJsonStringFromObject(sldStudentMapper.toStructure(student.get())));// need to convert to structure MANDATORY otherwise jackson will break.
      event.setEventOutcome(EventOutcome.SLD_STUDENT_UPDATED);
    } else {
      event.setEventOutcome(EventOutcome.SLD_STUDENT_NOT_FOUND);
    }

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
    final SldService<SldStudentId, SldStudentEntity> service = (SldService<SldStudentId, SldStudentEntity>) this.sldServiceMap.get(EntityName.STUDENT);
    final var students = service.restore(updateEvent.getPen(), sldStudentMapper.toModel(updateEvent.getSldStudent()));
    event.setEventPayload(JsonUtil.getJsonStringFromObject(students.stream().map(sldStudentMapper::toStructure).collect(Collectors.toList())));// need to convert to structure MANDATORY otherwise jackson will break.
    event.setEventOutcome(EventOutcome.SLD_STUDENT_RESTORED);

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
    final SldService<SldStudentProgramId, SldStudentProgramEntity> service = (SldService<SldStudentProgramId, SldStudentProgramEntity>) this.sldServiceMap.get(EntityName.STUDENT_PROGRAMS);
    final var students = service.updateBatch(sldUpdateStudentProgramsEventMapper.toSldStudentProgramEntity(updateEvent), sldStudentProgramMapper.toModel(updateEvent.getSldStudentProgram()));
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
    final SldService<SldStudentProgramId, SldStudentProgramEntity> service = (SldService<SldStudentProgramId, SldStudentProgramEntity>) this.sldServiceMap.get(EntityName.STUDENT_PROGRAMS);
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
