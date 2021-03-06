package ca.bc.gov.educ.api.sld.struct.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type sld student history.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SldStudentHistory {
  private String distNo;
  private String schlNo;
  private long reportDate;
  private String localStudentId;
  private String enrolledGradeCode;
  private String sex;
  private String birthDate;
  private String legalSurname;
  private String legalGivenName;
  private String legalMiddleName;
  private String usualSurname;
  private String usualGivenName;
  private String usualMiddleName;
  private String pen;
  private String source;
  private String postal;
}
