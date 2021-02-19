package ca.bc.gov.educ.api.sld.struct.v1;

import ca.bc.gov.educ.api.sld.constant.EventOutcome;
import ca.bc.gov.educ.api.sld.constant.EventType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


/**
 * The type Event.
 */
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {
  private EventType eventType;
  private EventOutcome eventOutcome;
  private String replyTo;
  private String eventPayload;
  private String payloadVersion;
  private UUID sagaId;

}
