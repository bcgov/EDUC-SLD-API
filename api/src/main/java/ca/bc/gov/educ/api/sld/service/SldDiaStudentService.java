package ca.bc.gov.educ.api.sld.service;

import ca.bc.gov.educ.api.sld.constant.EventOutcome;
import ca.bc.gov.educ.api.sld.constant.EventType;
import ca.bc.gov.educ.api.sld.helpers.PersistenceHelper;
import ca.bc.gov.educ.api.sld.model.SldDiaStudentEntity;
import ca.bc.gov.educ.api.sld.repository.EventRepository;
import ca.bc.gov.educ.api.sld.struct.v1.Event;
import ca.bc.gov.educ.api.sld.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SldDiaStudentService {
  public static final String NO_RECORD_SAGA_ID_EVENT_TYPE = "no record found for the saga id and event type combination, processing";
  public static final String RECORD_FOUND_FOR_SAGA_ID_EVENT_TYPE = "record found for the saga id and event type combination, might be a duplicate or replay," +
    " just updating the db status so that it will be polled and sent back again.";
  private final EntityManagerFactory emf;
  private final EventRepository eventRepository;

  public SldDiaStudentService(final EntityManagerFactory emf, final EventRepository eventRepository) {
    this.emf = emf;
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
      final var updateStatements = this.prepareStatement(sldDiaStudentEntities);
      final int count = PersistenceHelper.bulkExecute(this.emf, updateStatements);
      event.setEventOutcome(EventOutcome.SLD_DIA_STUDENTS_CREATED);
      event.setEventPayload(String.valueOf(count));
      sldEvent = createEvent(event);
    }
    this.eventRepository.save(sldEvent);
    return createResponseEvent(sldEvent);
  }

  private List<String> prepareStatement(final List<SldDiaStudentEntity> entities) {
    final List<String> statements = new ArrayList<>();
    for (val entity : entities) {
      val builder = new StringBuilder();
      builder
        .append("INSERT INTO DIA_STUDENT ") // end with beginning single quote
        .append("(DISTNO, SCHLNO, RECORD_NUMBER, REPORT_DATE, PEN, STUD_SURNAME, STUD_GIVEN, STUD_MIDDLE, USUAL_SURNAME, USUAL_GIVEN,")
        .append(" STUD_BIRTH, STUD_SEX, STUD_GRADE, FTE_VAL, AGREEMENT_TYPE, SCHOOL_NAME, SCHBOARD, SCHNUM, SCHTYPE, BANDNAME, FRBANDNUM,")
        .append(" BANDRESNUM, BAND_CODE, PEN_STATUS, ORIG_PEN, SITENO, WITHDRAWAL_CODE, DIA_SCHOOL_INFO_WRONG, DISTNO_NEW, SCHLNO_NEW,")
        .append(" SITENO_NEW, STUD_NEW_FLAG, PEN_COMMENT, POSTED_PEN) VALUES (")
        .append("'")
        .append(entity.getSldDiaStudentId().getDistNo())
        .append("', '")
        .append(entity.getSldDiaStudentId().getSchlNo())
        .append("', ")
        .append(entity.getSldDiaStudentId().getRecordNumber())
        .append(", ")
        .append(entity.getSldDiaStudentId().getReportDate())
        .append(", '")
        .append(entity.getPen())
        .append("', '")
        .append(StringUtils.defaultString(entity.getStudSurname()).replace("'", "''"))
        .append("', '")
        .append(StringUtils.defaultString(entity.getStudGiven()).replace("'", "''"))
        .append("', '")
        .append(StringUtils.defaultString(entity.getStudMiddle()).replace("'", "''"))
        .append("', '")
        .append(StringUtils.defaultString(entity.getUsualSurname()).replace("'", "''"))
        .append("', '")
        .append(StringUtils.defaultString(entity.getUsualGiven()).replace("'", "''"))
        .append("', '")
        .append(StringUtils.defaultString(entity.getStudBirth()))
        .append("', '")
        .append(StringUtils.defaultString(entity.getStudSex()))
        .append("', '")
        .append(StringUtils.defaultString(entity.getStudGrade()))
        .append("', ")
        .append(entity.getFteVal())
        .append(", '")
        .append(StringUtils.defaultString(entity.getAgreementType()))
        .append("', '")
        .append(StringUtils.defaultString(entity.getSchoolName()).replace("'", "''"))
        .append("', '")
        .append(StringUtils.defaultString(entity.getSchboard()).replace("'", "''"))
        .append("', '")
        .append(StringUtils.defaultString(entity.getSchnum()))
        .append("', '")
        .append(StringUtils.defaultString(entity.getSchtype()))
        .append("', '")
        .append(StringUtils.defaultString(entity.getBandname()).replace("'", "''"))
        .append("', '")
        .append(StringUtils.defaultString(entity.getFrbandnum()))
        .append("', '")
        .append(StringUtils.defaultString(entity.getBandresnum()))
        .append("', '")
        .append(StringUtils.defaultString(entity.getBandCode()))
        .append("', '")
        .append(StringUtils.defaultString(entity.getPenStatus()))
        .append("', '")
        .append(StringUtils.defaultString(entity.getOrigPen()))
        .append("', '")
        .append(StringUtils.defaultString(entity.getSiteno()))
        .append("', '")
        .append(StringUtils.defaultString(entity.getWithdrawalCode()))
        .append("', '")
        .append(StringUtils.defaultString(entity.getDiaSchoolInfoWrong()))
        .append("', '")
        .append(StringUtils.defaultString(entity.getDistnoNew()))
        .append("', '")
        .append(StringUtils.defaultString(entity.getSchlnoNew()))
        .append("', '")
        .append(StringUtils.defaultString(entity.getSitenoNew()))
        .append("', '")
        .append(StringUtils.defaultString(entity.getStudNewFlag()))
        .append("', '")
        .append(StringUtils.defaultString(entity.getPenComment()).replace("'", "''"))
        .append("', '")
        .append(StringUtils.defaultString(entity.getPostedPen()))
        .append("')");
      statements.add(builder.toString());
    }
    return statements;
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
