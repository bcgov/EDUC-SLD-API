package ca.bc.gov.educ.api.sld.service;

import ca.bc.gov.educ.api.sld.controller.v1.BaseSLDAPITest;
import ca.bc.gov.educ.api.sld.mappers.v1.SldDiaStudentMapper;
import ca.bc.gov.educ.api.sld.mappers.v1.SldStudentMapper;
import ca.bc.gov.educ.api.sld.mappers.v1.SldStudentProgramMapper;
import ca.bc.gov.educ.api.sld.messaging.MessagePublisher;
import ca.bc.gov.educ.api.sld.repository.SldDiaStudentRepository;
import ca.bc.gov.educ.api.sld.repository.SldRepository;
import ca.bc.gov.educ.api.sld.repository.SldStudentProgramRepository;
import ca.bc.gov.educ.api.sld.struct.v1.Event;
import ca.bc.gov.educ.api.sld.support.SldTestUtil;
import ca.bc.gov.educ.api.sld.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import io.nats.client.Message;
import lombok.val;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.BasicJsonTester;

import java.io.IOException;
import java.util.stream.Collectors;

import static ca.bc.gov.educ.api.sld.constant.EventOutcome.*;
import static ca.bc.gov.educ.api.sld.constant.EventType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class EventHandlerDelegatorServiceTest extends BaseSLDAPITest {
  private static final SldStudentMapper sldStudentMapper = SldStudentMapper.mapper;
  private static final SldDiaStudentMapper sldDiaStudentMapper = SldDiaStudentMapper.mapper;
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
  private SldDiaStudentService sldDiaStudentService;
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
    this.sldDiaStudentRepository.deleteAll();
    this.sldStudentProgramRepository.deleteAll();
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
    assertThat(students).extractingJsonPathNumberValue("$.length()").isEqualTo(7);
    assertThat(students).extractingJsonPathStringValue("$[0].pen").contains(newPen);
    assertThat(students).hasJsonPath("$[0].enrolledGradeCode");
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
   * Test handle UPDATE_SLD_DIA_STUDENTS event.
   */
  @Test
  public void testHandleEvent_givenEventTypeUPDATE_SLD_DIA_STUDENTS_shouldUpdateSldDiaStudentsAndSendEvent() throws IOException {

    SldTestUtil.createSampleDBData("SldDiaStudentSampleData.json", new TypeReference<>() {
    }, this.sldDiaStudentRepository, sldDiaStudentMapper::toModel);
    final var payload = this.dummyUpdateSldPenJson(pen, "sldDiaStudent", newPen);
    final var topic = "api-topic";
    final var event = Event.builder().eventType(UPDATE_SLD_DIA_STUDENTS).payloadVersion("V1").eventPayload(payload).replyTo(topic).build();

    this.eventHandlerDelegatorService.handleEvent(event, this.message);
    verify(this.messagePublisher, atMostOnce()).dispatchMessage(eq(topic), this.eventCaptor.capture());
    val results = this.sldDiaStudentService.findExistingStudentsByPen(newPen).stream().map(el -> el.getSldDiaStudentId().getPen()).collect(Collectors.toList());
    assertThat(results).contains(newPen + "D").size().isEqualTo(4);
    final var replyEvent = JsonUtil.getJsonObjectFromString(Event.class, new String(this.eventCaptor.getValue()));
    assertThat(replyEvent.getEventType()).isEqualTo(UPDATE_SLD_DIA_STUDENTS);
    assertThat(replyEvent.getEventOutcome()).isEqualTo(SLD_DIA_STUDENT_UPDATED);

    final var students = jsonTester.from(replyEvent.getEventPayload().getBytes());
    assertThat(students).extractingJsonPathNumberValue("$.length()").isEqualTo(4);
    assertThat(students).extractingJsonPathStringValue("$[0].pen").contains(newPen);
    assertThat(students).hasJsonPath("$[0].diaSchoolInfoWrong");
  }

  /**
   * Test handle UPDATE_SLD_DIA_STUDENTS event.
   */
  @Test
  public void testHandleEvent_givenEventTypeUPDATE_SLD_DIA_STUDENTS_and_NonExistentPen_shouldReturnEmptyList() throws IOException {

    SldTestUtil.createSampleDBData("SldDiaStudentSampleData.json", new TypeReference<>() {
    }, this.sldDiaStudentRepository, sldDiaStudentMapper::toModel);
    final var payload = this.dummyUpdateSldPenJson(nonExistentPen, "sldDiaStudent", newPen);
    final var topic = "api-topic";
    final var event = Event.builder().eventType(UPDATE_SLD_DIA_STUDENTS).payloadVersion("V1").eventPayload(payload).replyTo(topic).build();

    this.eventHandlerDelegatorService.handleEvent(event, this.message);
    verify(this.messagePublisher, atMostOnce()).dispatchMessage(eq(topic), this.eventCaptor.capture());

    final var replyEvent = JsonUtil.getJsonObjectFromString(Event.class, new String(this.eventCaptor.getValue()));
    assertThat(replyEvent.getEventType()).isEqualTo(UPDATE_SLD_DIA_STUDENTS);
    assertThat(replyEvent.getEventOutcome()).isEqualTo(SLD_DIA_STUDENT_UPDATED);

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
    assertThat(students).extractingJsonPathNumberValue("$.length()").isEqualTo(7);
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

  protected String dummyUpdateSldPenJson(final String pen, final String recordName, final String newPen) {
    return " {\n" +
      "    \"pen\": \"" + pen + "\",\n" +
      "    \"" + recordName + "\": {\n" +
      "    \"pen\": \"" + newPen + "\"\n" +
      "    }\n" +
      "  }";
  }

}
