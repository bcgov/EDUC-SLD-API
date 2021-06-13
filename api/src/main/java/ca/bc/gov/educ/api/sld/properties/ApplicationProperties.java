package ca.bc.gov.educ.api.sld.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Class holds all application properties
 *
 * @author Marco Villeneuve
 */
@Component
@Getter
public class ApplicationProperties {
  public static final String CORRELATION_ID = "correlationID";
  /**
   * The constant API_NAME.
   */
  public static final String API_NAME = "SLD_API";

  @Value("${nats.server}")
  private String server;

  @Value("${nats.maxReconnect}")
  private int maxReconnect;

  @Value("${nats.connectionName}")
  private String connectionName;
}
