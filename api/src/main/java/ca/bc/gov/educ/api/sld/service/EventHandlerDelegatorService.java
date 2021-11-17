package ca.bc.gov.educ.api.sld.service;

import ca.bc.gov.educ.api.sld.messaging.MessagePublisher;
import ca.bc.gov.educ.api.sld.struct.v1.Event;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.nats.client.Message;
import lombok.extern.slf4j.Slf4j;
import org.jboss.threads.EnhancedQueueExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;


/**
 * The type Event handler service.
 */
@Service
@Slf4j
@SuppressWarnings({"java:S3864", "java:S3776"})
public class EventHandlerDelegatorService {

  /**
   * The constant RESPONDING_BACK_TO_NATS_ON_CHANNEL.
   */
  public static final String RESPONDING_BACK_TO_NATS_ON_CHANNEL = "responding back to NATS on {} channel ";
  /**
   * The constant PAYLOAD_LOG.
   */
  public static final String PAYLOAD_LOG = "payload is :: {}";
  private final ExecutorService diaExecutor = new EnhancedQueueExecutor.Builder().setThreadFactory(new ThreadFactoryBuilder().setNameFormat("dia-student-executor-%d").build())
    .setCorePoolSize(1).setMaximumPoolSize(1).build();
  private final MessagePublisher messagePublisher;
  private final EventHandlerService eventHandlerService;

  /**
   * Instantiates a new Event handler delegator service.
   *
   * @param messagePublisher    the message publisher
   * @param eventHandlerService the event handler service
   */
  @Autowired
  public EventHandlerDelegatorService(final MessagePublisher messagePublisher, final EventHandlerService eventHandlerService) {
    this.messagePublisher = messagePublisher;
    this.eventHandlerService = eventHandlerService;
  }

  /**
   * Handle event.
   *
   * @param event   the event
   * @param message the message
   */
  public void handleEvent(final Event event, final Message message) {
    final byte[] response;
    final boolean isSynchronous = message.getReplyTo() != null;
    try {
      switch (event.getEventType()) {
        case UPDATE_SLD_STUDENTS:
          log.info("received update sld students event :: {}", event.getSagaId());
          log.trace(PAYLOAD_LOG, event.getEventPayload());
          response = this.eventHandlerService.handleUpdateStudentsEvent(event);
          log.info(RESPONDING_BACK_TO_NATS_ON_CHANNEL, message.getReplyTo() != null ? message.getReplyTo() : event.getReplyTo());
          this.publishToNATS(event, message, isSynchronous, response);
          break;
        case UPDATE_SLD_STUDENTS_BY_IDS:
          log.info("received update sld students by ids event :: {}", event.getSagaId());
          log.trace(PAYLOAD_LOG, event.getEventPayload());
          response = this.eventHandlerService.handleUpdateStudentsByIdsEvent(event);
          log.info(RESPONDING_BACK_TO_NATS_ON_CHANNEL, message.getReplyTo() != null ? message.getReplyTo() : event.getReplyTo());
          this.publishToNATS(event, message, isSynchronous, response);
          break;
        case RESTORE_SLD_STUDENTS:
          log.info("received restore sld students event :: {}", event.getSagaId());
          log.trace(PAYLOAD_LOG, event.getEventPayload());
          response = this.eventHandlerService.handleRestoreStudentsEvent(event);
          log.info(RESPONDING_BACK_TO_NATS_ON_CHANNEL, message.getReplyTo() != null ? message.getReplyTo() : event.getReplyTo());
          this.publishToNATS(event, message, isSynchronous, response);
          break;
        case UPDATE_SLD_STUDENT_PROGRAMS:
          log.info("received update sld student programs event :: {}", event.getSagaId());
          log.trace(PAYLOAD_LOG, event.getEventPayload());
          response = this.eventHandlerService.handleUpdateStudentProgramsEvent(event);
          log.info(RESPONDING_BACK_TO_NATS_ON_CHANNEL, message.getReplyTo() != null ? message.getReplyTo() : event.getReplyTo());
          this.publishToNATS(event, message, isSynchronous, response);
          break;
        case UPDATE_SLD_STUDENT_PROGRAMS_BY_DATA:
          log.info("received update sld student programs by data event :: {}", event.getSagaId());
          log.trace(PAYLOAD_LOG, event.getEventPayload());
          response = this.eventHandlerService.handleUpdateStudentProgramsByDataEvent(event);
          log.info(RESPONDING_BACK_TO_NATS_ON_CHANNEL, message.getReplyTo() != null ? message.getReplyTo() : event.getReplyTo());
          this.publishToNATS(event, message, isSynchronous, response);
          break;
        case RESTORE_SLD_STUDENT_PROGRAMS:
          log.info("received restore sld student programs event :: {}", event.getSagaId());
          log.trace(PAYLOAD_LOG, event.getEventPayload());
          response = this.eventHandlerService.handleRestoreStudentProgramsEvent(event);
          log.info(RESPONDING_BACK_TO_NATS_ON_CHANNEL, message.getReplyTo() != null ? message.getReplyTo() : event.getReplyTo());
          this.publishToNATS(event, message, isSynchronous, response);
          break;
        case CREATE_SLD_DIA_STUDENTS:
          this.diaExecutor.execute(() -> {
            try {
              log.info("received create sld dia students event :: {}", event.getSagaId());
              log.trace(PAYLOAD_LOG, event.getEventPayload());
              final byte[] createSldDiaStudents = this.eventHandlerService.handleCreateSldDiaStudents(event);
              log.info(RESPONDING_BACK_TO_NATS_ON_CHANNEL, message.getReplyTo() != null ? message.getReplyTo() : event.getReplyTo());
              this.publishToNATS(event, message, isSynchronous, createSldDiaStudents);
            } catch (final JsonProcessingException e) {
              log.error("error while processing event :: {}", event.getSagaId(), e);
            }
          });
          break;

        default:
          log.info("silently ignoring other events :: {}", event);
          break;
      }
    } catch (final Exception e) {
      log.error("Exception", e);
    }
  }

  private void publishToNATS(final Event event, final Message message, final boolean isSynchronous, final byte[] response) {
    if (isSynchronous) { // sync, req/reply pattern of nats
      this.messagePublisher.dispatchMessage(message.getReplyTo(), response);
    } else { // async, pub/sub
      this.messagePublisher.dispatchMessage(event.getReplyTo(), response);
    }
  }

}
