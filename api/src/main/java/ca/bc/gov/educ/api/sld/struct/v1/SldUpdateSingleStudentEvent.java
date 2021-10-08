package ca.bc.gov.educ.api.sld.struct.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type sld update single student event.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SldUpdateSingleStudentEvent {
  /**
   * the PEN which is used to search the sld students.
   */
  private String pen;
  /**
   * the distNo which is used to search the sld students.
   */
  private String distNo;
  /**
   * the schlNo which is used to search the sld students.
   */
  private String schlNo;
  /**
   * the reportDate which is used to search the sld students.
   */
  private Long reportDate;
  /**
   * the attributes of sld record to be updated.
   * Leave the attribute null if no update.
   */
  private SldStudent sldStudent;
}
