package ca.bc.gov.educ.api.sld.service;

import ca.bc.gov.educ.api.sld.messaging.MessagePublisher;
import ca.bc.gov.educ.api.sld.struct.v1.Event;
import io.nats.client.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
  }

  /**
   * Handle event.
   *
   * @param event   the event
   * @param message the message
   */
  public void handleEvent(final Event event, final Message message) {
    byte[] response;
    boolean isSynchronous = message.getReplyTo() != null;
    try {
      switch (event.getEventType()) {
        case UPDATE_SLD_STUDENTS:
          log.info("received update sld students event :: {}", event.getSagaId());
          log.trace(PAYLOAD_LOG, event.getEventPayload());
          response = eventHandlerService.handleUpdateStudentsEvent(event);
          log.info(RESPONDING_BACK_TO_NATS_ON_CHANNEL, message.getReplyTo() != null ? message.getReplyTo() : event.getReplyTo());
          publishToNATS(event, message, isSynchronous, response);
          break;
        case UPDATE_SLD_DIA_STUDENTS:
          log.info("received update sld dia students event :: {}", event.getSagaId());
          log.trace(PAYLOAD_LOG, event.getEventPayload());
          response = eventHandlerService.handleUpdateDiaStudentsEvent(event);
          log.info(RESPONDING_BACK_TO_NATS_ON_CHANNEL, message.getReplyTo() != null ? message.getReplyTo() : event.getReplyTo());
          publishToNATS(event, message, isSynchronous, response);
          break;
        case UPDATE_SLD_STUDENT_PROGRAMS:
          log.info("received update sld student programs event :: {}", event.getSagaId());
          log.trace(PAYLOAD_LOG, event.getEventPayload());
          response = eventHandlerService.handleUpdateStudentProgramsEvent(event);
          log.info(RESPONDING_BACK_TO_NATS_ON_CHANNEL, message.getReplyTo() != null ? message.getReplyTo() : event.getReplyTo());
          publishToNATS(event, message, isSynchronous, response);
          break;
        default:
          log.info("silently ignoring other events :: {}", event);
          break;
      }
    } catch (final Exception e) {
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
