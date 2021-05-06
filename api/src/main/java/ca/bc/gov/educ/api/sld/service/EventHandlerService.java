package ca.bc.gov.educ.api.sld.service;

import ca.bc.gov.educ.api.sld.constant.EventOutcome;
import ca.bc.gov.educ.api.sld.mappers.v1.SldDiaStudentMapper;
import ca.bc.gov.educ.api.sld.mappers.v1.SldStudentMapper;
import ca.bc.gov.educ.api.sld.mappers.v1.SldStudentProgramMapper;
import ca.bc.gov.educ.api.sld.struct.v1.Event;
import ca.bc.gov.educ.api.sld.struct.v1.SldUpdateDiaStudentsEvent;
import ca.bc.gov.educ.api.sld.struct.v1.SldUpdateStudentProgramsEvent;
import ca.bc.gov.educ.api.sld.struct.v1.SldUpdateStudentsEvent;
import ca.bc.gov.educ.api.sld.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

/**
 * The type Event handler service.
 */
@Service
@Slf4j
@SuppressWarnings("java:S3864")
public class EventHandlerService {

  private static final SldStudentMapper sldStudentMapper = SldStudentMapper.mapper;
  private static final SldDiaStudentMapper sldDiaStudentMapper = SldDiaStudentMapper.mapper;
  private static final SldStudentProgramMapper sldStudentProgramMapper = SldStudentProgramMapper.mapper;

  @Getter(PRIVATE)
  private final SldStudentService sldStudentService;

  @Getter(PRIVATE)
  private final SldDiaStudentService sldDiaStudentService;

  @Getter(PRIVATE)
  private final SldStudentProgramService sldStudentProgramService;

  /**
   * Instantiates a new Event handler service.
   *
   * @param sldStudentService      the sld student service
   */
  @Autowired
  public EventHandlerService(final SldStudentService sldStudentService, final SldDiaStudentService sldDiaStudentService, final SldStudentProgramService sldStudentProgramService) {
    this.sldStudentService = sldStudentService;
    this.sldDiaStudentService = sldDiaStudentService;
    this.sldStudentProgramService = sldStudentProgramService;
  }

  /**
   * Handle update sld students event.
   *
   * @param event the event
   * @return the byte [ ]
   */
  @SneakyThrows(JsonProcessingException.class)
  public byte[] handleUpdateStudentsEvent(final Event event) {
    final var updateEvent = JsonUtil.getJsonObjectFromString(SldUpdateStudentsEvent.class, event.getEventPayload());
    final var students = this.getSldStudentService().updateSldStudentsByPen(updateEvent.getPen(), updateEvent.getSldStudent());
    event.setEventPayload(JsonUtil.getJsonStringFromObject(students.stream().map(sldStudentMapper::toStructure).collect(Collectors.toList())));// need to convert to structure MANDATORY otherwise jackson will break.
    event.setEventOutcome(EventOutcome.SLD_STUDENT_UPDATED);

    return this.createResponseEvent(event);
  }

  /**
   * Handle update sld dia students event.
   *
   * @param event the event
   * @return the byte [ ]
   */
  @SneakyThrows(JsonProcessingException.class)
  public byte[] handleUpdateDiaStudentsEvent(final Event event) {
    final var updateEvent = JsonUtil.getJsonObjectFromString(SldUpdateDiaStudentsEvent.class, event.getEventPayload());
    final var students = this.getSldDiaStudentService().updateDiaStudentsByPen(updateEvent.getPen(), updateEvent.getSldDiaStudent());
    event.setEventPayload(JsonUtil.getJsonStringFromObject(students.stream().map(sldDiaStudentMapper::toStructure).collect(Collectors.toList())));// need to convert to structure MANDATORY otherwise jackson will break.
    event.setEventOutcome(EventOutcome.SLD_DIA_STUDENT_UPDATED);

    return this.createResponseEvent(event);
  }

  /**
   * Handle update sld student programs event.
   *
   * @param event the event
   * @return the byte [ ]
   */
  @SneakyThrows(JsonProcessingException.class)
  public byte[] handleUpdateStudentProgramsEvent(final Event event){
    final var updateEvent = JsonUtil.getJsonObjectFromString(SldUpdateStudentProgramsEvent.class, event.getEventPayload());
    final var students = this.getSldStudentProgramService().updateStudentProgramsByPen(updateEvent.getPen(), updateEvent.getSldStudentProgram());
    event.setEventPayload(JsonUtil.getJsonStringFromObject(students.stream().map(sldStudentProgramMapper::toStructure).collect(Collectors.toList())));// need to convert to structure MANDATORY otherwise jackson will break.
    event.setEventOutcome(EventOutcome.SLD_STUDENT_PROGRAM_UPDATED);
    return this.createResponseEvent(event);
  }

  @SneakyThrows(JsonProcessingException.class)
  public byte[] handleGetStudentsEvent(final Event event) {
    val students = this.getSldStudentService().getSldByPen(event.getEventPayload());
    event.setEventPayload(JsonUtil.getJsonStringFromObject(students.stream().map(sldStudentMapper::toStructure).collect(Collectors.toList())));// need to convert to structure MANDATORY otherwise jackson will break.
    event.setEventOutcome(EventOutcome.SLD_STUDENT_FOUND);
    return this.createResponseEvent(event);
  }
  @SneakyThrows(JsonProcessingException.class)
  public byte[] handleGetDiaStudentsEvent(final Event event) {
    val students = this.getSldDiaStudentService().getDiaStudentByPen(event.getEventPayload());
    event.setEventPayload(JsonUtil.getJsonStringFromObject(students.stream().map(sldDiaStudentMapper::toStructure).collect(Collectors.toList())));// need to convert to structure MANDATORY otherwise jackson will break.
    event.setEventOutcome(EventOutcome.SLD_DIA_STUDENT_FOUND);
    return this.createResponseEvent(event);
  }

  @SneakyThrows(JsonProcessingException.class)
  public byte[] handleGetStudentProgramsEvent(Event event) {
    val students = this.getSldStudentProgramService().getStudentProgramsByPen(event.getEventPayload());
    event.setEventPayload(JsonUtil.getJsonStringFromObject(students.stream().map(sldStudentProgramMapper::toStructure).collect(Collectors.toList())));// need to convert to structure MANDATORY otherwise jackson will break.
    event.setEventOutcome(EventOutcome.SLD_STUDENT_PROGRAM_FOUND);
    return this.createResponseEvent(event);
  }
    private byte[] createResponseEvent(final Event event) throws JsonProcessingException {
    return JsonUtil.getJsonBytesFromObject(Event.builder()
      .sagaId(event.getSagaId())
      .eventType(event.getEventType())
      .eventOutcome(event.getEventOutcome())
      .eventPayload(event.getEventPayload()).build());
  }


}
