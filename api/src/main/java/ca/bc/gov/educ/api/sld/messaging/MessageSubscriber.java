package ca.bc.gov.educ.api.sld.messaging;

import ca.bc.gov.educ.api.sld.service.EventHandlerDelegatorService;
import ca.bc.gov.educ.api.sld.struct.v1.Event;
import ca.bc.gov.educ.api.sld.util.JsonUtil;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.MessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.jboss.threads.EnhancedQueueExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executor;

import static ca.bc.gov.educ.api.sld.constant.Topics.SLD_API_TOPIC;

/**
 * This is for subscribing directly to NATS.
 */
@Component
@Slf4j
public class MessageSubscriber {
  private final Executor messageProcessingThreads;
  private final EventHandlerDelegatorService eventHandlerDelegatorServiceV1;
  private final Connection connection;

  /**
   * Instantiates a new Message subscriber.
   *
   * @param connection                 the nats connection
   * @param eventHandlerDelegatorServiceV1 the event handler delegator service
   */
  @Autowired
  public MessageSubscriber(final Connection connection, EventHandlerDelegatorService eventHandlerDelegatorServiceV1) {
    this.eventHandlerDelegatorServiceV1 = eventHandlerDelegatorServiceV1;
    this.connection = connection;
    messageProcessingThreads = new EnhancedQueueExecutor.Builder().setThreadFactory(new ThreadFactoryBuilder().setNameFormat("nats-message-subscriber-%d").build())
      .setCorePoolSize(1).setMaximumPoolSize(1).build();
  }

  /**
   * Subscribe to SLD_API_TOPIC.
   */
  @PostConstruct
  public void subscribe() {
    String queue = SLD_API_TOPIC.toString().replace("_", "-");
    var dispatcher = connection.createDispatcher(onMessage());
    dispatcher.subscribe(SLD_API_TOPIC.toString(), queue);
  }

  /**
   * On message message handler.
   *
   * @return the message handler
   */
  private MessageHandler onMessage() {
    return (Message message) -> {
      if (message != null) {
        try {
          var eventString = new String(message.getData());
          var event = JsonUtil.getJsonObjectFromString(Event.class, eventString);
          if (event.getPayloadVersion() == null) {
            event.setPayloadVersion("V1");
          }
          //place holder to have different versions
          if ("V1".equalsIgnoreCase(event.getPayloadVersion())) {
            messageProcessingThreads.execute(() -> eventHandlerDelegatorServiceV1.handleEvent(event, message));
          }
        } catch (final Exception e) {
          log.error("Exception ", e);
        }
      }
    };
  }


}
