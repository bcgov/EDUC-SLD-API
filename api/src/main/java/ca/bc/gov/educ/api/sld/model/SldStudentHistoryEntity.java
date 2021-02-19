package ca.bc.gov.educ.api.sld.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;

/**
 * The type sld student history entity.
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Immutable
@Table(name = "STUDENT_HISTORY_VW")
public class SldStudentHistoryEntity {

  @EmbeddedId
  private SldStudentHistoryId sldStudentHistoryId;

  @Column(name = "VDISTNO")
  private String distNo;

  @Column(name = "VSCHLNO")
  private String schlNo;

  @Column(name = "VENROLLED_GRADE_CODE")
  private String enrolledGradeCode;

  @Column(name = "VSEX")
  private String sex;

  @Column(name = "VBIRTH_DATE")
  private String birthDate;

  @Column(name = "VLEGAL_SURNAME")
  private String legalSurname;

  @Column(name = "VLEGAL_GIVEN_NAME")
  private String legalGivenName;

  @Column(name = "VLEGAL_MIDDLE_NAME")
  private String legalMiddleName;

  @Column(name = "VUSUAL_SURNAME")
  private String usualSurname;

  @Column(name = "VUSUAL_GIVEN_NAME")
  private String usualGivenName;

  @Column(name = "VUSUAL_MIDDLE_NAME")
  private String usualMiddleName;

  @Column(name = "VPEN")
  private String pen;

  @Column(name = "VSOURCE")
  private String source;

  @Column(name = "VPOSTAL")
  private String postal;
}
