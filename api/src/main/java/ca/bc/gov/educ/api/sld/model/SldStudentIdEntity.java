package ca.bc.gov.educ.api.sld.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * The type Sld Student Id.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class SldStudentIdEntity implements Serializable {
  @Column(name = "STUDENT_ID", nullable = false)
  private String studentId;

  @Column(name = "REPORT_DATE", nullable = false)
  private long reportDate;
}
