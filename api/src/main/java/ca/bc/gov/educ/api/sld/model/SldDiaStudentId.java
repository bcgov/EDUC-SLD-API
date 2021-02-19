package ca.bc.gov.educ.api.sld.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * The type Sld DIA Student Id.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class SldDiaStudentId implements Serializable {
  @Column(name = "PEN", nullable = false)
  private String pen;

  @Column(name = "REPORT_DATE", nullable = false)
  private Long reportDate;
}
