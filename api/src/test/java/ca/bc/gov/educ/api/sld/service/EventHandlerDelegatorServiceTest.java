package ca.bc.gov.educ.api.sld.service;

import ca.bc.gov.educ.api.sld.controller.v1.BaseSLDAPITest;
import ca.bc.gov.educ.api.sld.mappers.v1.SldStudentMapper;
import ca.bc.gov.educ.api.sld.mappers.v1.SldStudentProgramMapper;
import ca.bc.gov.educ.api.sld.messaging.MessagePublisher;
import ca.bc.gov.educ.api.sld.repository.SldDiaStudentRepository;
import ca.bc.gov.educ.api.sld.repository.SldRepository;
import ca.bc.gov.educ.api.sld.repository.SldStudentProgramRepository;
import ca.bc.gov.educ.api.sld.struct.v1.Event;
import ca.bc.gov.educ.api.sld.struct.v1.SldDiaStudent;
import ca.bc.gov.educ.api.sld.support.SldTestUtil;
import ca.bc.gov.educ.api.sld.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import io.nats.client.Message;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.BasicJsonTester;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static ca.bc.gov.educ.api.sld.constant.EventOutcome.*;
import static ca.bc.gov.educ.api.sld.constant.EventType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class EventHandlerDelegatorServiceTest extends BaseSLDAPITest {
  private static final SldStudentMapper sldStudentMapper = SldStudentMapper.mapper;
  private static final SldStudentProgramMapper sldStudentProgramMapper = SldStudentProgramMapper.mapper;
  private static final BasicJsonTester jsonTester = new BasicJsonTester(EventHandlerDelegatorServiceTest.class);
  private static final String pen = "120164447";
  private static final String nonExistentPen = "000000000";
  private static final String newPen = "100100010";
  @Captor
  ArgumentCaptor<byte[]> eventCaptor;
  @Autowired
  private SldStudentProgramService sldStudentProgramService;
  @Autowired
  private SldStudentService sldStudentService;
  @Autowired
  private EventHandlerService eventHandlerService;
  @Autowired
  private SldRepository sldRepository;
  @Autowired
  private SldDiaStudentRepository sldDiaStudentRepository;
  @Autowired
  private SldStudentProgramRepository sldStudentProgramRepository;
  @Mock
  private MessagePublisher messagePublisher;
  @Mock
  private Message message;
  private EventHandlerDelegatorService eventHandlerDelegatorService;

  @Before
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    this.eventHandlerDelegatorService = new EventHandlerDelegatorService(this.messagePublisher, this.eventHandlerService);

  }

  @After
  public void after() {
    this.sldRepository.deleteAll();
    this.sldStudentProgramRepository.deleteAll();
    this.sldDiaStudentRepository.deleteAll();
  }

  /**
   * Test handle UPDATE_SLD_STUDENTS event.
   */
  @Test
  public void testHandleEvent_givenEventTypeUPDATE_SLD_STUDENTS_shouldUpdateSldStudentsAndSendEvent() throws IOException {

    SldTestUtil.createSampleDBData("SldSampleStudentData.json", new TypeReference<>() {
    }, this.sldRepository, sldStudentMapper::toModel);
    final var payload = this.dummyUpdateSldPenJson(pen, "sldStudent", newPen);
    final var topic = "api-topic";
    final var event = Event.builder().eventType(UPDATE_SLD_STUDENTS).payloadVersion("V1").eventPayload(payload).replyTo(topic).build();

    this.eventHandlerDelegatorService.handleEvent(event, this.message);
    verify(this.messagePublisher, atMostOnce()).dispatchMessage(eq(topic), this.eventCaptor.capture());

    final var replyEvent = JsonUtil.getJsonObjectFromString(Event.class, new String(this.eventCaptor.getValue()));
    assertThat(replyEvent.getEventType()).isEqualTo(UPDATE_SLD_STUDENTS);
    assertThat(replyEvent.getEventOutcome()).isEqualTo(SLD_STUDENT_UPDATED);
    val results = this.sldStudentService.findExistingStudentsByPen(newPen).stream().map(el -> el.getSldStudentId().getPen()).collect(Collectors.toList());
    assertThat(results).contains(newPen + "D", newPen + "E").size().isEqualTo(7);
    final var students = jsonTester.from(replyEvent.getEventPayload().getBytes());
    assertThat(students).extractingJsonPathNumberValue("$.length()").isEqualTo(3);
    assertThat(students).extractingJsonPathStringValue("$[0].pen").contains(newPen);
    assertThat(students).hasJsonPath("$[0].enrolledGradeCode");
  }

  @Test
  public void testHandleEvent_givenEventTypeUPDATE_SLD_STUDENTS2_shouldUpdateSldStudentsAndSendEvent() throws IOException {

    SldTestUtil.createSampleDBData("SldSampleStudentData2.json", new TypeReference<>() {
    }, this.sldRepository, sldStudentMapper::toModel);
    final var payload = this.dummyUpdateSldPenJson("111014502", "sldStudent", "110885621");
    final var topic = "api-topic";
    final var event = Event.builder().eventType(UPDATE_SLD_STUDENTS).payloadVersion("V1").eventPayload(payload).replyTo(topic).build();

    this.eventHandlerDelegatorService.handleEvent(event, this.message);
    verify(this.messagePublisher, atMostOnce()).dispatchMessage(eq(topic), this.eventCaptor.capture());

    final var replyEvent = JsonUtil.getJsonObjectFromString(Event.class, new String(this.eventCaptor.getValue()));
    assertThat(replyEvent.getEventType()).isEqualTo(UPDATE_SLD_STUDENTS);
    assertThat(replyEvent.getEventOutcome()).isEqualTo(SLD_STUDENT_UPDATED);
    val results = this.sldStudentService.findExistingStudentsByPen("110885621").stream().map(el -> el.getSldStudentId().getPen()).collect(Collectors.toList());
    assertThat(results).contains("110885621E", "110885621F").size().isEqualTo(9);
    final var students = jsonTester.from(replyEvent.getEventPayload().getBytes());
    assertThat(students).extractingJsonPathNumberValue("$.length()").isEqualTo(4);
    assertThat(students).extractingJsonPathStringValue("$[0].pen").contains("110885621");
  }

  /**
   * Test handle UPDATE_SLD_STUDENTS event.
   */
  @Test
  public void testHandleEvent_givenEventTypeUPDATE_SLD_STUDENTS_and_NonExistentPen_shouldReturnEmptyList() throws IOException {

    SldTestUtil.createSampleDBData("SldSampleStudentData.json", new TypeReference<>() {
    }, this.sldRepository, sldStudentMapper::toModel);
    final var payload = this.dummyUpdateSldPenJson(nonExistentPen, "sldStudent", newPen);
    final var topic = "api-topic";
    final var event = Event.builder().eventType(UPDATE_SLD_STUDENTS).payloadVersion("V1").eventPayload(payload).replyTo(topic).build();

    this.eventHandlerDelegatorService.handleEvent(event, this.message);
    verify(this.messagePublisher, atMostOnce()).dispatchMessage(eq(topic), this.eventCaptor.capture());

    final var replyEvent = JsonUtil.getJsonObjectFromString(Event.class, new String(this.eventCaptor.getValue()));
    assertThat(replyEvent.getEventType()).isEqualTo(UPDATE_SLD_STUDENTS);
    assertThat(replyEvent.getEventOutcome()).isEqualTo(SLD_STUDENT_UPDATED);

    final var students = jsonTester.from(replyEvent.getEventPayload().getBytes());
    assertThat(students).extractingJsonPathNumberValue("$.length()").isEqualTo(0);
  }

  /**
   * Test handle UPDATE_SLD_STUDENT_PROGRAMS event.
   */
  @Test
  public void testHandleEvent_givenEventTypeUPDATE_SLD_STUDENT_PROGRAMS_shouldUpdateSldStudentProgramsAndSendEvent() throws IOException {

    SldTestUtil.createSampleDBData("SldStudentProgramSampleData.json", new TypeReference<>() {
    }, this.sldStudentProgramRepository, sldStudentProgramMapper::toModel);
    final var payload = this.dummyUpdateSldPenJson(pen, "sldStudentProgram", newPen);
    final var topic = "api-topic";
    final var event = Event.builder().eventType(UPDATE_SLD_STUDENT_PROGRAMS).payloadVersion("V1").eventPayload(payload).replyTo(topic).build();

    this.eventHandlerDelegatorService.handleEvent(event, this.message);
    verify(this.messagePublisher, atMostOnce()).dispatchMessage(eq(topic), this.eventCaptor.capture());

    final var replyEvent = JsonUtil.getJsonObjectFromString(Event.class, new String(this.eventCaptor.getValue()));
    assertThat(replyEvent.getEventType()).isEqualTo(UPDATE_SLD_STUDENT_PROGRAMS);
    assertThat(replyEvent.getEventOutcome()).isEqualTo(SLD_STUDENT_PROGRAM_UPDATED);

    val results = this.sldStudentProgramService.findExistingSLDStudentProgramsByPen(newPen).stream().map(el -> el.getSldStudentProgramId().getPen()).collect(Collectors.toList());

    assertThat(results).contains(newPen + "D", newPen + "E").size().isEqualTo(7);
    final var students = jsonTester.from(replyEvent.getEventPayload().getBytes());
    assertThat(students).extractingJsonPathNumberValue("$.length()").isEqualTo(3);
    assertThat(students).extractingJsonPathStringValue("$[0].pen").contains(newPen);
    assertThat(students).hasJsonPath("$[0].enrolledProgramCode");
  }

  /**
   * Test handle UPDATE_SLD_STUDENT_PROGRAMS event.
   */
  @Test
  public void testHandleEvent_givenEventTypeUPDATE_SLD_STUDENT_PROGRAMS_shouldUpdateSomeSldStudentProgramsAndSendEvent() throws IOException {

    SldTestUtil.createSampleDBData("SldStudentProgramSampleData.json", new TypeReference<>() {
    }, this.sldStudentProgramRepository, sldStudentProgramMapper::toModel);
    final var payload = this.dummyUpdateSldPenJson(pen, "069", "69015", 20030930, "120164447", "sldStudentProgram", newPen);
    final var topic = "api-topic";
    final var event = Event.builder().eventType(UPDATE_SLD_STUDENT_PROGRAMS).payloadVersion("V1").eventPayload(payload).replyTo(topic).build();

    this.eventHandlerDelegatorService.handleEvent(event, this.message);
    verify(this.messagePublisher, atMostOnce()).dispatchMessage(eq(topic), this.eventCaptor.capture());

    final var replyEvent = JsonUtil.getJsonObjectFromString(Event.class, new String(this.eventCaptor.getValue()));
    assertThat(replyEvent.getEventType()).isEqualTo(UPDATE_SLD_STUDENT_PROGRAMS);
    assertThat(replyEvent.getEventOutcome()).isEqualTo(SLD_STUDENT_PROGRAM_UPDATED);

    val results = this.sldStudentProgramService.findExistingSLDStudentProgramsByPen(newPen).stream().map(el -> el.getSldStudentProgramId().getPen()).collect(Collectors.toList());

    assertThat(results).contains(newPen + "D").size().isEqualTo(7);
    final var students = jsonTester.from(replyEvent.getEventPayload().getBytes());
    assertThat(students).extractingJsonPathNumberValue("$.length()").isEqualTo(3);
    assertThat(students).extractingJsonPathStringValue("$[0].pen").contains(newPen);
    assertThat(students).hasJsonPath("$[0].enrolledProgramCode");
  }

  /**
   * Test handle UPDATE_SLD_STUDENT_PROGRAMS event.
   */
  @Test
  public void testHandleEvent_givenEventTypeUPDATE_SLD_STUDENT_PROGRAMS_and_NonExistentPen_shouldReturnEmptyList() throws IOException {

    SldTestUtil.createSampleDBData("SldStudentProgramSampleData.json", new TypeReference<>() {
    }, this.sldStudentProgramRepository, sldStudentProgramMapper::toModel);
    final var payload = this.dummyUpdateSldPenJson(nonExistentPen, "sldStudentProgram", newPen);
    final var topic = "api-topic";
    final var event = Event.builder().eventType(UPDATE_SLD_STUDENT_PROGRAMS).payloadVersion("V1").eventPayload(payload).replyTo(topic).build();

    this.eventHandlerDelegatorService.handleEvent(event, this.message);
    verify(this.messagePublisher, atMostOnce()).dispatchMessage(eq(topic), this.eventCaptor.capture());

    final var replyEvent = JsonUtil.getJsonObjectFromString(Event.class, new String(this.eventCaptor.getValue()));
    assertThat(replyEvent.getEventType()).isEqualTo(UPDATE_SLD_STUDENT_PROGRAMS);
    assertThat(replyEvent.getEventOutcome()).isEqualTo(SLD_STUDENT_PROGRAM_UPDATED);

    final var students = jsonTester.from(replyEvent.getEventPayload().getBytes());
    assertThat(students).extractingJsonPathNumberValue("$.length()").isEqualTo(0);
  }

  /**
   * Test handle RESTORE_SLD_STUDENTS event.
   */
  @Test
  public void testHandleEvent_givenEventTypeRESTORE_SLD_STUDENTS_shouldRestoreSldStudentsAndSendEvent() throws IOException {

    SldTestUtil.createSampleDBData("SldSampleStudentDataForDemerge.json", new TypeReference<>() {
    }, this.sldRepository, sldStudentMapper::toModel);
    final var payload = this.dummyUpdateSldPenJson(pen, "sldStudent", newPen);
    final var topic = "api-topic";
    final var event = Event.builder().eventType(RESTORE_SLD_STUDENTS).payloadVersion("V1").eventPayload(payload).replyTo(topic).build();

    this.eventHandlerDelegatorService.handleEvent(event, this.message);
    verify(this.messagePublisher, atMostOnce()).dispatchMessage(eq(topic), this.eventCaptor.capture());

    final var replyEvent = JsonUtil.getJsonObjectFromString(Event.class, new String(this.eventCaptor.getValue()));
    assertThat(replyEvent.getEventType()).isEqualTo(RESTORE_SLD_STUDENTS);
    assertThat(replyEvent.getEventOutcome()).isEqualTo(SLD_STUDENT_RESTORED);
    val results = this.sldStudentService.findExistingStudentsByPen(pen).stream().map(el -> el.getSldStudentId().getPen()).collect(Collectors.toList());
    assertThat(results).contains(pen + "D").size().isEqualTo(4);
    final var students = jsonTester.from(replyEvent.getEventPayload().getBytes());
    assertThat(students).extractingJsonPathNumberValue("$.length()").isEqualTo(4);
    assertThat(students).extractingJsonPathStringValue("$[0].pen").contains(pen);
    assertThat(students).hasJsonPath("$[0].enrolledGradeCode");
  }

  /**
   * Test handle RESTORE_SLD_STUDENTS event.
   */
  @Test
  public void testHandleEvent_givenEventTypeRESTORE_SLD_STUDENTS_and_NonExistentPen_shouldReturnEmptyList() throws IOException {

    SldTestUtil.createSampleDBData("SldSampleStudentDataForDemerge.json", new TypeReference<>() {
    }, this.sldRepository, sldStudentMapper::toModel);
    final var payload = this.dummyUpdateSldPenJson(nonExistentPen, "sldStudent", newPen);
    final var topic = "api-topic";
    final var event = Event.builder().eventType(RESTORE_SLD_STUDENTS).payloadVersion("V1").eventPayload(payload).replyTo(topic).build();

    this.eventHandlerDelegatorService.handleEvent(event, this.message);
    verify(this.messagePublisher, atMostOnce()).dispatchMessage(eq(topic), this.eventCaptor.capture());

    final var replyEvent = JsonUtil.getJsonObjectFromString(Event.class, new String(this.eventCaptor.getValue()));
    assertThat(replyEvent.getEventType()).isEqualTo(RESTORE_SLD_STUDENTS);
    assertThat(replyEvent.getEventOutcome()).isEqualTo(SLD_STUDENT_RESTORED);

    final var students = jsonTester.from(replyEvent.getEventPayload().getBytes());
    assertThat(students).extractingJsonPathNumberValue("$.length()").isEqualTo(0);
  }

  /**
   * Test handle RESTORE_SLD_STUDENTS event.
   */
  @Test
  public void testHandleEvent_givenEventTypeRESTORE_SLD_STUDENT_PROGRAMS_shouldRestoreSldStudentsAndSendEvent() throws IOException {
    SldTestUtil.createSampleDBData("SldStudentProgramSampleDataForDemerge.json", new TypeReference<>() {
    }, this.sldStudentProgramRepository, sldStudentProgramMapper::toModel);
    final var payload = this.dummyUpdateSldPenJson(pen, "sldStudentProgram", newPen);
    final var topic = "api-topic";
    final var event = Event.builder().eventType(RESTORE_SLD_STUDENT_PROGRAMS).payloadVersion("V1").eventPayload(payload).replyTo(topic).build();

    this.eventHandlerDelegatorService.handleEvent(event, this.message);
    verify(this.messagePublisher, atMostOnce()).dispatchMessage(eq(topic), this.eventCaptor.capture());

    final var replyEvent = JsonUtil.getJsonObjectFromString(Event.class, new String(this.eventCaptor.getValue()));
    assertThat(replyEvent.getEventType()).isEqualTo(RESTORE_SLD_STUDENT_PROGRAMS);
    assertThat(replyEvent.getEventOutcome()).isEqualTo(SLD_STUDENT_PROGRAM_RESTORED);
    val results = this.sldStudentProgramService.findExistingSLDStudentProgramsByPen(pen).stream().map(el -> el.getSldStudentProgramId().getPen()).collect(Collectors.toList());

    assertThat(results).contains(pen + "D").size().isEqualTo(4);
    final var students = jsonTester.from(replyEvent.getEventPayload().getBytes());
    assertThat(students).extractingJsonPathNumberValue("$.length()").isEqualTo(4);
    assertThat(students).extractingJsonPathStringValue("$[0].pen").contains(pen);
    assertThat(students).hasJsonPath("$[0].enrolledProgramCode");
  }

  /**
   * Test handle RESTORE_SLD_STUDENTS event.
   */
  @Test
  public void testHandleEvent_givenEventTypeRESTORE_SLD_STUDENT_PROGRAMS_and_NonExistentPen_shouldReturnEmptyList() throws IOException {

    SldTestUtil.createSampleDBData("SldStudentProgramSampleDataForDemerge.json", new TypeReference<>() {
    }, this.sldStudentProgramRepository, sldStudentProgramMapper::toModel);
    final var payload = this.dummyUpdateSldPenJson(nonExistentPen, "sldStudentProgram", newPen);
    final var topic = "api-topic";
    final var event = Event.builder().eventType(RESTORE_SLD_STUDENT_PROGRAMS).payloadVersion("V1").eventPayload(payload).replyTo(topic).build();

    this.eventHandlerDelegatorService.handleEvent(event, this.message);
    verify(this.messagePublisher, atMostOnce()).dispatchMessage(eq(topic), this.eventCaptor.capture());

    final var replyEvent = JsonUtil.getJsonObjectFromString(Event.class, new String(this.eventCaptor.getValue()));
    assertThat(replyEvent.getEventType()).isEqualTo(RESTORE_SLD_STUDENT_PROGRAMS);
    assertThat(replyEvent.getEventOutcome()).isEqualTo(SLD_STUDENT_PROGRAM_RESTORED);

    final var students = jsonTester.from(replyEvent.getEventPayload().getBytes());
    assertThat(students).extractingJsonPathNumberValue("$.length()").isEqualTo(0);
  }

  /**
   * Test handle UPDATE_SLD_STUDENTS_BY_IDS event.
   */
  @Test
  public void testHandleEvent_givenEventTypeUPDATE_SLD_STUDENTS_BY_IDS_shouldUpdateSldStudentsAndSendEvent() throws IOException {

    SldTestUtil.createSampleDBData("SldSampleStudentData.json", new TypeReference<>() {
    }, this.sldRepository, sldStudentMapper::toModel);
    final var payload = this.dummyUpdateSldPensJson(pen, null, null, "ids", "sldStudent", newPen);
    final var topic = "api-topic";
    final var event = Event.builder().eventType(UPDATE_SLD_STUDENTS_BY_IDS).payloadVersion("V1").eventPayload(payload).replyTo(topic).build();

    this.eventHandlerDelegatorService.handleEvent(event, this.message);
    verify(this.messagePublisher, atMostOnce()).dispatchMessage(eq(topic), this.eventCaptor.capture());

    final var replyEvent = JsonUtil.getJsonObjectFromString(Event.class, new String(this.eventCaptor.getValue()));
    assertThat(replyEvent.getEventType()).isEqualTo(UPDATE_SLD_STUDENTS_BY_IDS);
    assertThat(replyEvent.getEventOutcome()).isEqualTo(SLD_STUDENT_UPDATED);
    val results = this.sldStudentService.findExistingStudentsByPen(newPen).stream().map(el -> el.getSldStudentId().getPen()).collect(Collectors.toList());
    assertThat(results).contains(newPen + "D", newPen + "E").size().isEqualTo(6);
    final var students = jsonTester.from(replyEvent.getEventPayload().getBytes());
    assertThat(students).extractingJsonPathNumberValue("$.length()").isEqualTo(2);
    assertThat(students).extractingJsonPathStringValue("$[0].pen").contains(newPen);
  }

  /**
   * Test handle UPDATE_SLD_STUDENT event.
   */
  @Test
  public void testHandleEvent_givenEventTypeUPDATE_SLD_STUDENTS_BY_IDS_and_NonExistentPen_shouldReturnSLD_STUDENT_NOT_FOUND() throws IOException {

    SldTestUtil.createSampleDBData("SldSampleStudentData.json", new TypeReference<>() {
    }, this.sldRepository, sldStudentMapper::toModel);
    final var payload = this.dummyUpdateSldPensJson(nonExistentPen, null, null, "ids","sldStudent", newPen);
    final var topic = "api-topic";
    final var event = Event.builder().eventType(UPDATE_SLD_STUDENTS_BY_IDS).payloadVersion("V1").eventPayload(payload).replyTo(topic).build();

    this.eventHandlerDelegatorService.handleEvent(event, this.message);
    verify(this.messagePublisher, atMostOnce()).dispatchMessage(eq(topic), this.eventCaptor.capture());

    final var replyEvent = JsonUtil.getJsonObjectFromString(Event.class, new String(this.eventCaptor.getValue()));
    assertThat(replyEvent.getEventType()).isEqualTo(UPDATE_SLD_STUDENTS_BY_IDS);
    assertThat(replyEvent.getEventOutcome()).isEqualTo(SLD_STUDENT_UPDATED);

    final var students = jsonTester.from(replyEvent.getEventPayload().getBytes());
    assertThat(students).extractingJsonPathNumberValue("$.length()").isEqualTo(0);

  }

  /**
   * Test handle UPDATE_SLD_STUDENT_PROGRAMS_BY_DATA event.
   */
  @Test
  public void testHandleEvent_givenEventTypeUPDATE_SLD_STUDENT_PROGRAMS_BY_DATA_shouldUpdateSomeSldStudentProgramsAndSendEvent() throws IOException {

    SldTestUtil.createSampleDBData("SldStudentProgramSampleData.json", new TypeReference<>() {
    }, this.sldStudentProgramRepository, sldStudentProgramMapper::toModel);
    final var payload = this.dummyUpdateSldPensJson(pen,"120164447", "120164447","examples", "sldStudentProgram", newPen);
    final var topic = "api-topic";
    final var event = Event.builder().eventType(UPDATE_SLD_STUDENT_PROGRAMS_BY_DATA).payloadVersion("V1").eventPayload(payload).replyTo(topic).build();

    this.eventHandlerDelegatorService.handleEvent(event, this.message);
    verify(this.messagePublisher, atMostOnce()).dispatchMessage(eq(topic), this.eventCaptor.capture());

    final var replyEvent = JsonUtil.getJsonObjectFromString(Event.class, new String(this.eventCaptor.getValue()));
    assertThat(replyEvent.getEventType()).isEqualTo(UPDATE_SLD_STUDENT_PROGRAMS_BY_DATA);
    assertThat(replyEvent.getEventOutcome()).isEqualTo(SLD_STUDENT_PROGRAM_UPDATED);

    val results = this.sldStudentProgramService.findExistingSLDStudentProgramsByPen(newPen).stream().map(el -> el.getSldStudentProgramId().getPen()).collect(Collectors.toList());

    assertThat(results).contains(newPen + "D").size().isEqualTo(5);
    final var students = jsonTester.from(replyEvent.getEventPayload().getBytes());
    assertThat(students).extractingJsonPathNumberValue("$.length()").isEqualTo(1);
    assertThat(students).extractingJsonPathStringValue("$[0].pen").contains(newPen);
    assertThat(students).hasJsonPath("$[0].enrolledProgramCode");
  }

  /**
   * Test handle UPDATE_SLD_STUDENT_PROGRAMS_BY_DATA event.
   */
  @Test
  public void testHandleEvent_givenEventTypeUPDATE_SLD_STUDENT_PROGRAMS_BY_DATA_with_multi_studentId_shouldUpdateSomeSldStudentProgramsAndSendEvent() throws IOException {

    SldTestUtil.createSampleDBData("SldStudentProgramSampleData.json", new TypeReference<>() {
    }, this.sldStudentProgramRepository, sldStudentProgramMapper::toModel);
    final var payload = this.dummyUpdateSldPensJson(pen,"120164446", "120164447","examples", "sldStudentProgram", newPen);
    final var topic = "api-topic";
    final var event = Event.builder().eventType(UPDATE_SLD_STUDENT_PROGRAMS_BY_DATA).payloadVersion("V1").eventPayload(payload).replyTo(topic).build();

    this.eventHandlerDelegatorService.handleEvent(event, this.message);
    verify(this.messagePublisher, atMostOnce()).dispatchMessage(eq(topic), this.eventCaptor.capture());

    final var replyEvent = JsonUtil.getJsonObjectFromString(Event.class, new String(this.eventCaptor.getValue()));
    assertThat(replyEvent.getEventType()).isEqualTo(UPDATE_SLD_STUDENT_PROGRAMS_BY_DATA);
    assertThat(replyEvent.getEventOutcome()).isEqualTo(SLD_STUDENT_PROGRAM_UPDATED);

    val results = this.sldStudentProgramService.findExistingSLDStudentProgramsByPen(newPen).stream().map(el -> el.getSldStudentProgramId().getPen()).collect(Collectors.toList());
    assertThat(results).contains(newPen + "D").size().isEqualTo(6);
    final var students = jsonTester.from(replyEvent.getEventPayload().getBytes());
    assertThat(students).extractingJsonPathNumberValue("$.length()").isEqualTo(2);
    assertThat(students).extractingJsonPathStringValue("$[0].pen").contains(newPen);
    assertThat(students).hasJsonPath("$[0].enrolledProgramCode");
  }

  /**
   * Test handle UPDATE_SLD_STUDENT_PROGRAMS_BY_DATA event.
   */
  @Test
  public void testHandleEvent_givenEventTypeUPDATE_SLD_STUDENT_PROGRAMS_BY_DATA_and_NonExistentPen_shouldReturnEmptyList() throws IOException {

    SldTestUtil.createSampleDBData("SldStudentProgramSampleData.json", new TypeReference<>() {
    }, this.sldStudentProgramRepository, sldStudentProgramMapper::toModel);
    final var payload = this.dummyUpdateSldPensJson(nonExistentPen,"120164447", "120164447", "examples", "sldStudentProgram", newPen);
    final var topic = "api-topic";
    final var event = Event.builder().eventType(UPDATE_SLD_STUDENT_PROGRAMS_BY_DATA).payloadVersion("V1").eventPayload(payload).replyTo(topic).build();

    this.eventHandlerDelegatorService.handleEvent(event, this.message);
    verify(this.messagePublisher, atMostOnce()).dispatchMessage(eq(topic), this.eventCaptor.capture());

    final var replyEvent = JsonUtil.getJsonObjectFromString(Event.class, new String(this.eventCaptor.getValue()));
    assertThat(replyEvent.getEventType()).isEqualTo(UPDATE_SLD_STUDENT_PROGRAMS_BY_DATA);
    assertThat(replyEvent.getEventOutcome()).isEqualTo(SLD_STUDENT_PROGRAM_UPDATED);

    final var students = jsonTester.from(replyEvent.getEventPayload().getBytes());
    assertThat(students).extractingJsonPathNumberValue("$.length()").isEqualTo(0);
  }

  @Test
  @SneakyThrows
  public void testHandleEvent_givenEventTypeCREATE_SLD_DIA_STUDENTS_shouldCreateSldDiaStudentsAndSendEvent() {

    final File file = new File(
      Objects.requireNonNull(SldTestUtil.class.getClassLoader().getResource("DIA_STUDENT.json")).getFile()
    );
    List<SldDiaStudent> sldDiaStudentList = JsonUtil.mapper.readValue(file, new TypeReference<>() {
    });
    SldTestUtil.createSampleDBData("SldSampleStudentData.json", new TypeReference<>() {
    }, this.sldRepository, sldStudentMapper::toModel);
    final var payload = JsonUtil.getJsonStringFromObject(sldDiaStudentList);
    final var topic = "api-topic";
    final var event = Event.builder().eventType(CREATE_SLD_DIA_STUDENTS).sagaId(UUID.randomUUID()).payloadVersion("V1").eventPayload(payload).replyTo("api-topic").build();

    this.eventHandlerDelegatorService.handleEvent(event, this.message);
    boolean isDataNotPresent = true;
    int counter = 0;
    while (isDataNotPresent) {
      if (sldDiaStudentRepository.count() > 0 || counter > 10) {
        isDataNotPresent = false;
      }
      counter++;
      TimeUnit.MILLISECONDS.sleep(20);
    }
    verify(this.messagePublisher, atMostOnce()).dispatchMessage(eq(topic), this.eventCaptor.capture());

    final var replyEvent = JsonUtil.getJsonObjectFromString(Event.class, new String(this.eventCaptor.getValue()));
    assertThat(replyEvent.getEventType()).isEqualTo(CREATE_SLD_DIA_STUDENTS);
    assertThat(replyEvent.getEventOutcome()).isEqualTo(SLD_DIA_STUDENTS_CREATED);
    assertThat(replyEvent.getEventPayload()).isNotBlank();
  }

  protected String dummyUpdateSldPenJson(final String pen, final String recordName, final String newPen) {
    return " {\n" +
      "    \"pen\": \"" + pen + "\",\n" +
      "    \"" + recordName + "\": {\n" +
      "    \"pen\": \"" + newPen + "\"\n" +
      "    }\n" +
      "  }";
  }

  protected String dummyUpdateSldPenJson(final String pen, final String distNo, final String schlNo, final long reportDate, final String studentId, final String recordName, final String newPen) {
    return " {\n" +
      "    \"pen\": \"" + pen + "\",\n" +
      "    \"distNo\": \"" + StringUtils.defaultString(distNo, "NULL") + "\",\n" +
      "    \"schlNo\": \"" + StringUtils.defaultString(schlNo, "NULL") + "\",\n" +
      "    \"reportDate\": \"" + reportDate + "\",\n" +
      "    \"studentId\": \"" + 	StringUtils.defaultString(studentId, "NULL") + "\",\n" +
      "    \"" + recordName + "\": {\n" +
      "    \"pen\": \"" + newPen + "\"\n" +
      "    }\n" +
      "  }";
  }

  protected String dummyUpdateSldPensJson(final String pen, final String studentId1, final String studentId2, final String keyName, final String recordName, final String newPen) {
    return " {\n" +
      "    \"" + keyName + "\": [{\n" +
      "      \"pen\": \"" + pen + "\",\n" +
      "      \"distNo\": \"069\",\n" +
      "      \"schlNo\": \"69015\",\n" +
      "      \"reportDate\": 20040201,\n" +
      "      \"studentId\": \"" + 	StringUtils.defaultString(studentId1, "NULL") + "\"\n" +
      "      },{\n" +
      "      \"pen\": \"" + pen + "\",\n" +
      "      \"distNo\": \"069\",\n" +
      "      \"schlNo\": \"69015\",\n" +
      "      \"reportDate\": 20040930,\n" +
      "      \"studentId\": \"" + 	StringUtils.defaultString(studentId2, "NULL") + "\"\n" +
      "      }],\n" +
      "    \"" + recordName + "\": {\n" +
      "    \"pen\": \"" + newPen + "\"\n" +
      "    }\n" +
      "  }";
  }

}
