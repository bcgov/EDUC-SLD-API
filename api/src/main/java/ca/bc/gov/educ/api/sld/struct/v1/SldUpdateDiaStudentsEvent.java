package ca.bc.gov.educ.api.sld.struct.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type sld update dia students event.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SldUpdateDiaStudentsEvent {
  /**
   * the PEN which is used to search the sld dia students.
   */
  private String pen;
  /**
   * the attributes of sld record to be updated.
   * Leave the attribute null if no update.
   */
  private SldDiaStudent sldDiaStudent;
}
