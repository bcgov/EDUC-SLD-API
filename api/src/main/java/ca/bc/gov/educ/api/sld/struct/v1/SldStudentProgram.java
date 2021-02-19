package ca.bc.gov.educ.api.sld.struct.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

/**
 * The type sld student program.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SldStudentProgram {
  @Size(max = 10)
  private String studentId;

  @Size(max = 3)
  private String distNo;

  @Size(max = 5)
  private String schlNo;

  private Long reportDate;

  @Size(max = 2)
  private String enrolledProgramCode;

  @Size(max = 2)
  private String careerProgram;

  @Size(max = 10)
  private String pen;

}
