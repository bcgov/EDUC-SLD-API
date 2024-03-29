package ca.bc.gov.educ.api.sld.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * The type Sld DIA Student Program entity.
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Immutable
@Table(name = "STUDENT_PROGRAMS")
public class SldStudentProgramEntity {

  @EmbeddedId
  private SldStudentProgramId sldStudentProgramId;

  @Column(name = "STUDENT_ID", nullable = false)
  private String studentId;

  @Column(name = "CAREER_PROGRAM")
  private String careerProgram;

}
