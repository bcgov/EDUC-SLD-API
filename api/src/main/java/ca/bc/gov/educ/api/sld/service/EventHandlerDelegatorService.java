package ca.bc.gov.educ.api.sld.service;

import ca.bc.gov.educ.api.sld.messaging.MessagePublisher;
import ca.bc.gov.educ.api.sld.struct.v1.Event;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.nats.client.Message;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jboss.threads.EnhancedQueueExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;


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
  private final MessagePublisher messagePublisher;
  private final EventHandlerService eventHandlerService;
  private final ExecutorService messageProcessingThread;

  /**
   * Instantiates a new Event handler delegator service.
   *
   * @param messagePublisher    the message publisher
   * @param eventHandlerService the event handler service
   */
  @Autowired
  public EventHandlerDelegatorService(MessagePublisher messagePublisher, EventHandlerService eventHandlerService) {
    this.messagePublisher = messagePublisher;
    this.eventHandlerService = eventHandlerService;
    messageProcessingThread = new EnhancedQueueExecutor.Builder().setThreadFactory(new ThreadFactoryBuilder().setNameFormat("nats-message-subscriber-%d").build())
      .setCorePoolSize(1).setMaximumPoolSize(1).build();
  }

  /**
   * Handle event.
   *
   * @param event   the event
   * @param message the message
   */
  public void handleEvent(final Event event, final Message message) {
    final byte[] response;
    boolean isSynchronous = message.getReplyTo() != null;
    try {
      switch (event.getEventType()) {
        case UPDATE_SLD_STUDENTS:
          val updateStudentsEvent = messageProcessingThread.submit( () -> {
            log.info("received update sld students event :: {}", event.getSagaId());
            log.trace(PAYLOAD_LOG, event.getEventPayload());
            return eventHandlerService.handleUpdateStudentsEvent(event);
          }).get(30, TimeUnit.SECONDS);
          log.info(RESPONDING_BACK_TO_NATS_ON_CHANNEL, message.getReplyTo() != null ? message.getReplyTo() : event.getReplyTo());
          publishToNATS(event, message, isSynchronous, updateStudentsEvent);
          break;
        case UPDATE_SLD_DIA_STUDENTS:
          val updateDiaStudentsEvent=
          messageProcessingThread.submit(()-> {
            log.info("received update sld dia students event :: {}", event.getSagaId());
            log.trace(PAYLOAD_LOG, event.getEventPayload());
            return  eventHandlerService.handleUpdateDiaStudentsEvent(event);
          }).get(30, TimeUnit.SECONDS);
          log.info(RESPONDING_BACK_TO_NATS_ON_CHANNEL, message.getReplyTo() != null ? message.getReplyTo() : event.getReplyTo());
          publishToNATS(event, message, isSynchronous, updateDiaStudentsEvent);
          break;
        case UPDATE_SLD_STUDENT_PROGRAMS:
          val updateStudentProgramsEvent = messageProcessingThread.submit(()-> {
            log.info("received update sld student programs event :: {}", event.getSagaId());
            log.trace(PAYLOAD_LOG, event.getEventPayload());
            return eventHandlerService.handleUpdateStudentProgramsEvent(event);
          }).get(30, TimeUnit.SECONDS);
          log.info(RESPONDING_BACK_TO_NATS_ON_CHANNEL, message.getReplyTo() != null ? message.getReplyTo() : event.getReplyTo());
          publishToNATS(event, message, isSynchronous, updateStudentProgramsEvent);
          break;
        case GET_SLD_STUDENTS:
          log.info("received get sld students event :: {}", event.getSagaId());
          log.trace(PAYLOAD_LOG, event.getEventPayload());
          response = eventHandlerService.handleGetStudentsEvent(event);
          log.info(RESPONDING_BACK_TO_NATS_ON_CHANNEL, message.getReplyTo() != null ? message.getReplyTo() : event.getReplyTo());
          publishToNATS(event, message, isSynchronous, response);
          break;
        case GET_SLD_DIA_STUDENTS:
          log.info("received get sld dia students event :: {}", event.getSagaId());
          log.trace(PAYLOAD_LOG, event.getEventPayload());
          response = eventHandlerService.handleGetDiaStudentsEvent(event);
          log.info(RESPONDING_BACK_TO_NATS_ON_CHANNEL, message.getReplyTo() != null ? message.getReplyTo() : event.getReplyTo());
          publishToNATS(event, message, isSynchronous, response);
          break;
        case GET_SLD_STUDENT_PROGRAMS:
          log.info("received get sld student programs event :: {}", event.getSagaId());
          log.trace(PAYLOAD_LOG, event.getEventPayload());
          response = eventHandlerService.handleGetStudentProgramsEvent(event);
          log.info(RESPONDING_BACK_TO_NATS_ON_CHANNEL, message.getReplyTo() != null ? message.getReplyTo() : event.getReplyTo());
          publishToNATS(event, message, isSynchronous, response);
          break;
        default:
          log.info("silently ignoring other events :: {}", event);
          break;
      }
    } catch (final Exception e) {
      Thread.currentThread().interrupt();
      log.error("Exception", e);
    }
  }

  private void publishToNATS(Event event, Message message, boolean isSynchronous, byte[] response) {
    if (isSynchronous) { // sync, req/reply pattern of nats
      messagePublisher.dispatchMessage(message.getReplyTo(), response);
    } else { // async, pub/sub
      messagePublisher.dispatchMessage(event.getReplyTo(), response);
    }
  }

}
