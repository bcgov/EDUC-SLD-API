package ca.bc.gov.educ.api.sld.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
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

  @Column(name = "DISTNO")
  private String distNo;

  @Column(name = "SCHLNO")
  private String schlNo;

  @Column(name = "RECORD_NUMBER")
  private Long recordNumber;


  @Column(name = "REPORT_DATE", nullable = false)
  private Long reportDate;
}
