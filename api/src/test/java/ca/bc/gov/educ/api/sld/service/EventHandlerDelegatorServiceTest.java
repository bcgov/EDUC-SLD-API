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

import static ca.bc.gov.educ.api.sld.constant.EventOutcome.*;
import static ca.bc.gov.educ.api.sld.constant.EventType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class EventHandlerDelegatorServiceTest extends BaseSLDAPITest {
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

  private static final SldStudentMapper sldStudentMapper = SldStudentMapper.mapper;
  private static final SldDiaStudentMapper sldDiaStudentMapper = SldDiaStudentMapper.mapper;
  private static final SldStudentProgramMapper sldStudentProgramMapper = SldStudentProgramMapper.mapper;

  private static final BasicJsonTester jsonTester = new BasicJsonTester(EventHandlerDelegatorServiceTest.class);

  private static final String pen = "120164447";
  private static final String nonExistentPen = "000000000";
  private static final String newPen = "10010001";

  private EventHandlerDelegatorService eventHandlerDelegatorService;

  @Captor
  ArgumentCaptor<byte[]> eventCaptor;


  @Before
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    eventHandlerDelegatorService = new EventHandlerDelegatorService(messagePublisher, eventHandlerService);

  }

  @After
  public void after() {
    sldRepository.deleteAll();
    sldDiaStudentRepository.deleteAll();
    sldStudentProgramRepository.deleteAll();
  }

  /**
   * Test handle UPDATE_SLD_STUDENTS event.
   *
   */
  @Test
  public void testHandleEvent_givenEventTypeUPDATE_SLD_STUDENTS_shouldUpdateSldStudentsAndSendEvent() throws IOException {

    SldTestUtil.createSampleDBData("SldSampleStudentData.json", new TypeReference<>() {}, sldRepository, sldStudentMapper::toModel);
    var payload = dummyUpdateSldPenJson(pen, "sldStudent", newPen);
    var topic = "api-topic";
    var event = Event.builder().eventType(UPDATE_SLD_STUDENTS).payloadVersion("V1").eventPayload(payload).replyTo(topic).build();

    eventHandlerDelegatorService.handleEvent(event, message);
    verify(messagePublisher, atMostOnce()).dispatchMessage(eq(topic), eventCaptor.capture());

    var replyEvent = JsonUtil.getJsonObjectFromString(Event.class, new String(eventCaptor.getValue()));
    assertThat(replyEvent.getEventType()).isEqualTo(UPDATE_SLD_STUDENTS);
    assertThat(replyEvent.getEventOutcome()).isEqualTo(SLD_STUDENT_UPDATED);

    var students = jsonTester.from(replyEvent.getEventPayload().getBytes());
    assertThat(students).extractingJsonPathNumberValue("$.length()").isEqualTo(3);
    assertThat(students).extractingJsonPathStringValue("$[0].pen").contains(newPen);
    assertThat(students).hasJsonPath("$[0].enrolledGradeCode");
  }

  /**
   * Test handle UPDATE_SLD_STUDENTS event.
   *
   */
  @Test
  public void testHandleEvent_givenEventTypeUPDATE_SLD_STUDENTS_and_NonExistentPen_shouldReturnEmptyList() throws IOException {

    SldTestUtil.createSampleDBData("SldSampleStudentData.json", new TypeReference<>() {}, sldRepository, sldStudentMapper::toModel);
    var payload = dummyUpdateSldPenJson(nonExistentPen, "sldStudent", newPen);
    var topic = "api-topic";
    var event = Event.builder().eventType(UPDATE_SLD_STUDENTS).payloadVersion("V1").eventPayload(payload).replyTo(topic).build();

    eventHandlerDelegatorService.handleEvent(event, message);
    verify(messagePublisher, atMostOnce()).dispatchMessage(eq(topic), eventCaptor.capture());

    var replyEvent = JsonUtil.getJsonObjectFromString(Event.class, new String(eventCaptor.getValue()));
    assertThat(replyEvent.getEventType()).isEqualTo(UPDATE_SLD_STUDENTS);
    assertThat(replyEvent.getEventOutcome()).isEqualTo(SLD_STUDENT_UPDATED);

    var students = jsonTester.from(replyEvent.getEventPayload().getBytes());
    assertThat(students).extractingJsonPathNumberValue("$.length()").isEqualTo(0);
  }

  /**
   * Test handle UPDATE_SLD_DIA_STUDENTS event.
   *
   */
  @Test
  public void testHandleEvent_givenEventTypeUPDATE_SLD_DIA_STUDENTS_shouldUpdateSldDiaStudentsAndSendEvent() throws IOException {

    SldTestUtil.createSampleDBData("SldDiaStudentSampleData.json", new TypeReference<>() {}, sldDiaStudentRepository, sldDiaStudentMapper::toModel);
    var payload = dummyUpdateSldPenJson(pen, "sldDiaStudent", newPen);
    var topic = "api-topic";
    var event = Event.builder().eventType(UPDATE_SLD_DIA_STUDENTS).payloadVersion("V1").eventPayload(payload).replyTo(topic).build();

    eventHandlerDelegatorService.handleEvent(event, message);
    verify(messagePublisher, atMostOnce()).dispatchMessage(eq(topic), eventCaptor.capture());

    var replyEvent = JsonUtil.getJsonObjectFromString(Event.class, new String(eventCaptor.getValue()));
    assertThat(replyEvent.getEventType()).isEqualTo(UPDATE_SLD_DIA_STUDENTS);
    assertThat(replyEvent.getEventOutcome()).isEqualTo(SLD_DIA_STUDENT_UPDATED);

    var students = jsonTester.from(replyEvent.getEventPayload().getBytes());
    assertThat(students).extractingJsonPathNumberValue("$.length()").isEqualTo(3);
    assertThat(students).extractingJsonPathStringValue("$[0].pen").contains(newPen);
    assertThat(students).hasJsonPath("$[0].diaSchoolInfoWrong");
  }

  /**
   * Test handle UPDATE_SLD_DIA_STUDENTS event.
   *
   */
  @Test
  public void testHandleEvent_givenEventTypeUPDATE_SLD_DIA_STUDENTS_and_NonExistentPen_shouldReturnEmptyList() throws IOException {

    SldTestUtil.createSampleDBData("SldDiaStudentSampleData.json", new TypeReference<>() {}, sldDiaStudentRepository, sldDiaStudentMapper::toModel);
    var payload = dummyUpdateSldPenJson(nonExistentPen, "sldDiaStudent", newPen);
    var topic = "api-topic";
    var event = Event.builder().eventType(UPDATE_SLD_DIA_STUDENTS).payloadVersion("V1").eventPayload(payload).replyTo(topic).build();

    eventHandlerDelegatorService.handleEvent(event, message);
    verify(messagePublisher, atMostOnce()).dispatchMessage(eq(topic), eventCaptor.capture());

    var replyEvent = JsonUtil.getJsonObjectFromString(Event.class, new String(eventCaptor.getValue()));
    assertThat(replyEvent.getEventType()).isEqualTo(UPDATE_SLD_DIA_STUDENTS);
    assertThat(replyEvent.getEventOutcome()).isEqualTo(SLD_DIA_STUDENT_UPDATED);

    var students = jsonTester.from(replyEvent.getEventPayload().getBytes());
    assertThat(students).extractingJsonPathNumberValue("$.length()").isEqualTo(0);
  }

  /**
   * Test handle UPDATE_SLD_STUDENT_PROGRAMS event.
   *
   */
  @Test
  public void testHandleEvent_givenEventTypeUPDATE_SLD_STUDENT_PROGRAMS_shouldUpdateSldStudentProgramsAndSendEvent() throws IOException {

    SldTestUtil.createSampleDBData("SldStudentProgramSampleData.json", new TypeReference<>() {}, sldStudentProgramRepository, sldStudentProgramMapper::toModel);
    var payload = dummyUpdateSldPenJson(pen, "sldStudentProgram", newPen);
    var topic = "api-topic";
    var event = Event.builder().eventType(UPDATE_SLD_STUDENT_PROGRAMS).payloadVersion("V1").eventPayload(payload).replyTo(topic).build();

    eventHandlerDelegatorService.handleEvent(event, message);
    verify(messagePublisher, atMostOnce()).dispatchMessage(eq(topic), eventCaptor.capture());

    var replyEvent = JsonUtil.getJsonObjectFromString(Event.class, new String(eventCaptor.getValue()));
    assertThat(replyEvent.getEventType()).isEqualTo(UPDATE_SLD_STUDENT_PROGRAMS);
    assertThat(replyEvent.getEventOutcome()).isEqualTo(SLD_STUDENT_PROGRAM_UPDATED);

    var students = jsonTester.from(replyEvent.getEventPayload().getBytes());
    assertThat(students).extractingJsonPathNumberValue("$.length()").isEqualTo(3);
    assertThat(students).extractingJsonPathStringValue("$[0].pen").contains(newPen);
    assertThat(students).hasJsonPath("$[0].enrolledProgramCode");
  }

  /**
   * Test handle UPDATE_SLD_STUDENT_PROGRAMS event.
   *
   */
  @Test
  public void testHandleEvent_givenEventTypeUPDATE_SLD_STUDENT_PROGRAMS_and_NonExistentPen_shouldReturnEmptyList() throws IOException {

    SldTestUtil.createSampleDBData("SldStudentProgramSampleData.json", new TypeReference<>() {}, sldStudentProgramRepository, sldStudentProgramMapper::toModel);
    var payload = dummyUpdateSldPenJson(nonExistentPen, "sldStudentProgram", newPen);
    var topic = "api-topic";
    var event = Event.builder().eventType(UPDATE_SLD_STUDENT_PROGRAMS).payloadVersion("V1").eventPayload(payload).replyTo(topic).build();

    eventHandlerDelegatorService.handleEvent(event, message);
    verify(messagePublisher, atMostOnce()).dispatchMessage(eq(topic), eventCaptor.capture());

    var replyEvent = JsonUtil.getJsonObjectFromString(Event.class, new String(eventCaptor.getValue()));
    assertThat(replyEvent.getEventType()).isEqualTo(UPDATE_SLD_STUDENT_PROGRAMS);
    assertThat(replyEvent.getEventOutcome()).isEqualTo(SLD_STUDENT_PROGRAM_UPDATED);

    var students = jsonTester.from(replyEvent.getEventPayload().getBytes());
    assertThat(students).extractingJsonPathNumberValue("$.length()").isEqualTo(0);
  }

  protected String dummyUpdateSldPenJson(String pen, String recordName, String newPen) {
    return " {\n" +
      "    \"pen\": \"" + pen  + "\",\n" +
      "    \"" + recordName + "\": {\n" +
      "    \"pen\": \"" + newPen + "\"\n" +
      "    }\n" +
      "  }";
  }

}
