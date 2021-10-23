package ca.bc.gov.educ.api.sld.struct.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * The type sld update dia student programs event.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SldUpdateStudentProgramsByDataEvent {
  /**
   * the examples which are used to search the sld student programs.
   */
  private List<SldStudentProgram> examples;

  /**
   * the attributes of sld record to be updated.
   * Leave the attribute null if no update.
   */
  private SldStudentProgram sldStudentProgram;
}
