package ca.bc.gov.educ.api.sld.struct.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * The type sld update single student event.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SldUpdateStudentsByIdsEvent {
  /**
   * the ids which are used to search the sld students.
   */
  private List<SldStudentId> ids;
  /**
   * the attributes of sld record to be updated.
   * Leave the attribute null if no update.
   */
  private SldStudent sldStudent;
}
