package ca.bc.gov.educ.api.sld.service;

import ca.bc.gov.educ.api.sld.controller.v1.BaseSLDAPITest;
import ca.bc.gov.educ.api.sld.mappers.v1.SldDiaStudentMapper;
import ca.bc.gov.educ.api.sld.mappers.v1.SldStudentMapper;
import ca.bc.gov.educ.api.sld.messaging.MessagePublisher;
import ca.bc.gov.educ.api.sld.repository.EventRepository;
import ca.bc.gov.educ.api.sld.repository.SldDiaStudentRepository;
import ca.bc.gov.educ.api.sld.repository.SldRepository;
import ca.bc.gov.educ.api.sld.struct.v1.Event;
import ca.bc.gov.educ.api.sld.struct.v1.SldDiaStudent;
import ca.bc.gov.educ.api.sld.support.SldTestUtil;
import ca.bc.gov.educ.api.sld.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.After;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.UUID;

import static ca.bc.gov.educ.api.sld.constant.EventType.CREATE_SLD_DIA_STUDENTS;
import static org.assertj.core.api.Assertions.assertThat;

public class SldDiaStudentServiceTest extends BaseSLDAPITest {
  private static final SldStudentMapper sldStudentMapper = SldStudentMapper.mapper;
  @Mock
  private MessagePublisher messagePublisher;
  @Autowired
  private SldRepository sldRepository;
  @Autowired
  private SldDiaStudentRepository sldDiaStudentRepository;
  @Autowired
  private EventRepository eventRepository;
  @Autowired
  private SldDiaStudentService service;

  @After
  public void after() {
    this.sldRepository.deleteAll();
    this.sldDiaStudentRepository.deleteAll();
    this.eventRepository.deleteAll();
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
    final var event = Event.builder().eventType(CREATE_SLD_DIA_STUDENTS).sagaId(UUID.randomUUID()).payloadVersion("V1").eventPayload(payload).replyTo("api-topic").build();
    val result = new String(service.createDiaStudents(sldDiaStudentList.stream().map(SldDiaStudentMapper.mapper::toModel).collect(Collectors.toList()), event));
    assertThat(result).isNotBlank();
    val events = eventRepository.findAll();
    val diaStudents = sldDiaStudentRepository.findAll();
    assertThat(diaStudents).hasSize(sldDiaStudentList.size());
    assertThat(events).hasSize(1);
    //call again to check it was not created again.
    service.createDiaStudents(sldDiaStudentList.stream().map(SldDiaStudentMapper.mapper::toModel).collect(Collectors.toList()), event);
    assertThat(diaStudents).hasSize(sldDiaStudentList.size());
    assertThat(events).hasSize(1);

  }
}
