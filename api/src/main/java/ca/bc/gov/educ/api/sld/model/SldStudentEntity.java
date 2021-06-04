package ca.bc.gov.educ.api.sld.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * The type sld student entity.
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Immutable
@Table(name = "STUDENT")
public class SldStudentEntity {

  @EmbeddedId
  private SldStudentId sldStudentId;


  @Column(name = "STUDENT_ID")
  private String studentId;

  @Column(name = "LOCAL_STUDENT_ID")
  private String localStudentId;

  @Column(name = "ADULT_TOTAL_CRS_HOURS")
  private String adultTotalCrsHours;

  @Column(name = "ENROLLED_GRADE_CODE")
  private String enrolledGradeCode;

  @Column(name = "STUDENT_FTE_VALUE")
  private String studentFteValue;

  @Column(name = "STUDENT_FUNDED_BY_CODE")
  private String studentFundedByCode;

  @Column(name = "NOT_ELIG_FOR_PROV_FUNDING")
  private String notEligForProvFunding;

  @Column(name = "NATIVE_ANCESTRY_IND")
  private String nativeAncestryInd;

  @Column(name = "QUALIFIED_IS_STUDENT_IND")
  private String qualifiedIsStudentInd;

  @Column(name = "SEX")
  private String sex;

  @Column(name = "BIRTH_DATE")
  private String birthDate;

  @Column(name = "HOME_LANGUAGE_SPOKEN")
  private String homeLanguageSpoken;

  @Column(name = "PROVINCE_CODE")
  private String provinceCode;

  @Column(name = "COUNTRY_CODE")
  private String countryCode;

  @Column(name = "LEGAL_SURNAME")
  private String legalSurname;

  @Column(name = "LEGAL_GIVEN_NAME")
  private String legalGivenName;

  @Column(name = "LEGAL_MIDDLE_NAME")
  private String legalMiddleName;

  @Column(name = "USUAL_SURNAME")
  private String usualSurname;

  @Column(name = "USUAL_GIVEN_NAME")
  private String usualGivenName;

  @Column(name = "USUAL_MIDDLE_NAME")
  private String usualMiddleName;


  @Column(name = "BAND_CODE")
  private String bandCode;

  @Column(name = "SCHOOL_FUNDING_CODE")
  private String schoolFundingCode;

  @Column(name = "NUMBER_OF_COURSES")
  private String numberOfCourses;

  @Column(name = "BIRTH_MONTH")
  private String birthMonth;

  @Column(name = "BIRTH_DAY")
  private String birthDay;

  @Column(name = "COLLEGE_COURSES")
  private String collegeCourses;

  @Column(name = "OTHER_COURSES")
  private String otherCourses;

  @Column(name = "POSTAL")
  private String postal;

  @Column(name = "SPED_CAT")
  private String spedCat;

  @Column(name = "SPED_DESC2")
  private String spedDesc2;

  @Column(name = "ADULT_GRAD")
  private String adultGrad;

  @Column(name = "FY_ADJUST")
  private String fyAdjust;

  @Column(name = "ESL_YRS")
  private String eslYrs;

  @Column(name = "FUND_FEB_SPED")
  private String fundFebSped;

  @Column(name = "NUMBER_OF_SUPPORT_BLOCKS")
  private String numberOfSupportBlocks;

  @Column(name = "GRAD")
  private String grad;
}
