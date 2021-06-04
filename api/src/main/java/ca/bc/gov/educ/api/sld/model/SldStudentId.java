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
public class SldStudentId implements Serializable {

  @Column(name = "DISTNO")
  private String distNo;

  @Column(name = "SCHLNO")
  private String schlNo;

  @Column(name = "PEN")
  private String pen;

  @Column(name = "REPORT_DATE", nullable = false)
  private Long reportDate;
}
