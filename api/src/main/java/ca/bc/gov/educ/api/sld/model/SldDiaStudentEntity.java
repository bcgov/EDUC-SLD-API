package ca.bc.gov.educ.api.sld.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * The type Sld DIA Student entity.
 */
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "DIA_STUDENT")
@Getter
@Setter
@ToString
public class SldDiaStudentEntity {

  @EmbeddedId
  private SldDiaStudentId sldDiaStudentId;

  @Column(name = "PEN", nullable = false)
  private String pen;

  @Column(name = "STUD_SURNAME")
  private String studSurname;

  @Column(name = "STUD_GIVEN")
  private String studGiven;

  @Column(name = "STUD_MIDDLE")
  private String studMiddle;

  @Column(name = "USUAL_SURNAME")
  private String usualSurname;

  @Column(name = "USUAL_GIVEN")
  private String usualGiven;

  @Column(name = "STUD_BIRTH")
  private String studBirth;

  @Column(name = "STUD_SEX")
  private String studSex;

  @Column(name = "STUD_GRADE")
  private String studGrade;

  @Column(name = "FTE_VAL")
  private Long fteVal;

  @Column(name = "AGREEMENT_TYPE")
  private String agreementType;

  @Column(name = "SCHOOL_NAME")
  private String schoolName;

  @Column(name = "SCHBOARD")
  private String schboard;

  @Column(name = "SCHNUM")
  private String schnum;

  @Column(name = "SCHTYPE")
  private String schtype;

  @Column(name = "BANDNAME")
  private String bandname;

  @Column(name = "FRBANDNUM")
  private String frbandnum;

  @Column(name = "BANDRESNUM")
  private String bandresnum;

  @Column(name = "BAND_CODE")
  private String bandCode;

  @Column(name = "PEN_STATUS")
  private String penStatus;

  @Column(name = "ORIG_PEN")
  private String origPen;

  @Column(name = "SITENO")
  private String siteno;

  @Column(name = "WITHDRAWAL_CODE")
  private String withdrawalCode;

  @Column(name = "DIA_SCHOOL_INFO_WRONG")
  private String diaSchoolInfoWrong;

  @Column(name = "DISTNO_NEW")
  private String distnoNew;

  @Column(name = "SCHLNO_NEW")
  private String schlnoNew;

  @Column(name = "SITENO_NEW")
  private String sitenoNew;

  @Column(name = "STUD_NEW_FLAG")
  private String studNewFlag;

  @Column(name = "PEN_COMMENT")
  private String penComment;

  @Column(name = "POSTED_PEN")
  private String postedPen;

}
