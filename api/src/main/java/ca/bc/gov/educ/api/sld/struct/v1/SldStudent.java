package ca.bc.gov.educ.api.sld.struct.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * The type sld student.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SldStudent {
  private String studentId;
  private String distNo;
  private String schlNo;
  private Long reportDate;
  private String localStudentId;
  private String adultTotalCrsHours;
  private String enrolledGradeCode;
  private String studentFteValue;
  private String studentFundedByCode;
  private String notEligForProvFunding;
  private String nativeAncestryInd;
  private String qualifiedIsStudentInd;
  private String sex;
  private String birthDate;
  private String homeLanguageSpoken;
  private String provinceCode;
  private String countryCode;
  private String legalSurname;
  private String legalGivenName;
  private String legalMiddleName;
  private String usualSurname;
  private String usualGivenName;
  private String usualMiddleName;
  private String pen;
  private String bandCode;
  private String schoolFundingCode;
  private String numberOfCourses;
  private String birthMonth;
  private String birthDay;
  private String collegeCourses;
  private String otherCourses;
  private String postal;
  private String spedCat;
  private String spedDesc2;
  private String adultGrad;
  private String fyAdjust;
  private String eslYrs;
  private String fundFebSped;
  private String numberOfSupportBlocks;
  private String grad;
}
