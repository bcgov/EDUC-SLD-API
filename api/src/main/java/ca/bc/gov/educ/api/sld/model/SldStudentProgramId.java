package ca.bc.gov.educ.api.sld.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;

/**
 * The type Sld Student Program Id.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class SldStudentProgramId implements Serializable {


  @Column(name = "REPORT_DATE", nullable = false)
  private Long reportDate;

  @Column(name = "DISTNO")
  private String distNo;

  @Column(name = "SCHLNO")
  private String schlNo;

  @Column(name = "ENROLLED_PROGRAM_CODE")
  private String enrolledProgramCode;

  @Column(name = "PEN")
  private String pen;
}
