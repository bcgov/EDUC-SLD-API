package ca.bc.gov.educ.api.sld.struct.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type sld update students event.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SldUpdateStudentsEvent {
  /**
   * the PEN which is used to search the sld students.
   */
  private String pen;
  /**
   * the attributes of sld record to be updated.
   * Leave the attribute null if no update.
   */
  private SldStudent sldStudent;
}
