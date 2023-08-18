package ca.bc.gov.educ.api.sld.struct.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Size;

/**
 * The type sld dia student.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SldDiaStudent {

  private Long reportDate;

  @Size(max = 3)
  private String distNo;

  @Size(max = 5)
  private String schlNo;

  private Long recordNumber;

  @Size(max = 10)
  private String pen;

  @Size(max = 25)
  private String studSurname;

  @Size(max = 25)
  private String studGiven;

  @Size(max = 25)
  private String studMiddle;

  @Size(max = 25)
  private String usualSurname;

  @Size(max = 25)
  private String usualGiven;

  @Size(max = 8)
  private String studBirth;

  @Size(max = 1)
  private String studSex;

  @Size(max = 2)
  private String studGrade;

  private Long fteVal;

  @Size(max = 1)
  private String agreementType;

  @Size(max = 40)
  private String schoolName;

  @Size(max = 3)
  private String schboard;

  @Size(max = 5)
  private String schnum;

  @Size(max = 20)
  private String schtype;

  @Size(max = 20)
  private String bandname;

  @Size(max = 20)
  private String frbandnum;

  @Size(max = 10)
  private String bandresnum;

  @Size(max = 5)
  private String bandCode;

  @Size(max = 70)
  private String comment;

  @Size(max = 2)
  private String penStatus;

  @Size(max = 10)
  private String origPen;

  @Size(max = 1)
  private String siteno;

  @Size(max = 2)
  private String withdrawalCode;

  @Size(max = 1)
  private String diaSchoolInfoWrong;

  @Size(max = 3)
  private String distnoNew;

  @Size(max = 5)
  private String schlnoNew;

  @Size(max = 1)
  private String sitenoNew;

  @Size(max = 1)
  private String studNewFlag;

  @Size(max = 30)
  private String penComment;

  @Size(max = 10)
  private String postedPen;
}
