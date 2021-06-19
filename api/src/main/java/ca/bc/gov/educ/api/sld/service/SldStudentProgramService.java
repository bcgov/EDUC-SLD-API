package ca.bc.gov.educ.api.sld.service;

import ca.bc.gov.educ.api.sld.model.SldStudentProgramEntity;
import ca.bc.gov.educ.api.sld.model.SldStudentProgramId;
import ca.bc.gov.educ.api.sld.repository.SldStudentProgramRepository;
import ca.bc.gov.educ.api.sld.struct.v1.SldStudentProgram;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import java.util.*;
import java.util.stream.Collectors;

import static ca.bc.gov.educ.api.sld.jooq.Tables.STUDENT_PROGRAMS;

/**
 * The type sld student program service.
 */
@Service
@Slf4j
public class SldStudentProgramService extends SldBaseService {

  @Getter(AccessLevel.PRIVATE)
  private final SldStudentProgramRepository sldStudentProgramRepository;

  /**
   * Instantiates a new sld student program service.
   *
   * @param emf                         the EntityManagerFactory
   * @param sldStudentProgramRepository the sld student program repo
   * @param create                      the DSLContext
   */
  @Autowired
  public SldStudentProgramService(final EntityManagerFactory emf, final SldStudentProgramRepository sldStudentProgramRepository, final DSLContext create) {
    super(emf, create, STUDENT_PROGRAMS, STUDENT_PROGRAMS.PEN);
    this.sldStudentProgramRepository = sldStudentProgramRepository;
  }


  /**
   * Update Student Programs by pen.
   *
   * @param pen               the PEN
   * @param sldStudentProgram the Sld Student Program data
   * @return the SldStudentProgramEntity list
   */
  public List<SldStudentProgramEntity> updateStudentProgramsByPen(final String pen, final SldStudentProgram sldStudentProgram) {
    final List<SldStudentProgramEntity> mergedFromPenData = this.findExistingSLDStudentProgramsByPen(pen);
    final List<SldStudentProgramEntity> mergedToPenData = this.findExistingSLDStudentProgramsByPen(sldStudentProgram.getPen());
    final List<String> updateStatements = this.prepareUpdateStatement(mergedFromPenData, mergedToPenData, sldStudentProgram.getPen());
    final int count = this.bulkUpdate(updateStatements);
    if (count > 0) {
      return this.findExistingSLDStudentProgramsByPen(pen.equals(sldStudentProgram.getPen()) ? pen : sldStudentProgram.getPen());
    } else {
      return List.of();
    }
  }

  /**
   * Find existing students by pen list.
   *
   * @param pen the pen
   * @return the list
   */
  protected List<SldStudentProgramEntity> findExistingSLDStudentProgramsByPen(final String pen) {
    final ExampleMatcher stringMatcher = ExampleMatcher.matchingAny()
      .withStringMatcher(ExampleMatcher.StringMatcher.STARTING);
    return this.getSldStudentProgramRepository().findAll(Example.of(SldStudentProgramEntity.builder().sldStudentProgramId(SldStudentProgramId.builder().pen(pen).build()).build(), stringMatcher));
  }

  private List<String> prepareUpdateStatement(final List<SldStudentProgramEntity> mergedFromPenData, final List<SldStudentProgramEntity> mergedToPenData, final String mergedToPen) {
    final List<String> updateStatements = new ArrayList<>();
    final Map<String, List<String>> distSchoolReportDatePenMap = this.createMergeToPenMap(mergedToPenData);
    for (val mergedFromPen : mergedFromPenData) {
      val key = this.getKey(mergedFromPen);
      Optional<String> highestPenOptional = Optional.empty();
      if (distSchoolReportDatePenMap.containsKey(key)) {
        final List<String> penList = distSchoolReportDatePenMap.get(key).stream()
          .sorted(Comparator.reverseOrder())
          .collect(Collectors.toList());
        final String highestPen = penList.get(0);
        final String nextPen;
        if (highestPen.trim().length() == 10) { // if it is 10 characters it already has a duplicate record.
          val lastCharacter = StringUtils.substring(highestPen, 9, 10);
          val index = duplicatePenSuffix.indexOf(lastCharacter);
          nextPen = StringUtils.substring(highestPen, 0, 9).concat(duplicatePenSuffix.get(index + 1)); // get the first 9 characters then append the next alphabet for the duplicate entry.
        } else {
          nextPen = highestPen.concat("D"); // first duplicate, starts with D
        }
        highestPenOptional = Optional.of(nextPen);
      }
      final String updatedPen = highestPenOptional.orElse(mergedToPen);
      updateStatements.add(this.createUpdateStatementForEachRecord(updatedPen, mergedFromPen));
    }
    return updateStatements;
  }

  /**
   * Create merge to pen map map.
   *
   * @param mergedToPenData the merged to pen data
   * @return the map
   */
  private Map<String, List<String>> createMergeToPenMap(final List<SldStudentProgramEntity> mergedToPenData) {
    final Map<String, List<String>> penMap = new HashMap<>();
    mergedToPenData.forEach(el -> {
      final String key = this.getKey(el);
      if (penMap.containsKey(key)) {
        val penList = penMap.get(key);
        penList.add(el.getSldStudentProgramId().getPen());
      } else {
        final List<String> penList = new ArrayList<>();
        penList.add(el.getSldStudentProgramId().getPen());
        penMap.put(key, penList);
      }
    });
    return penMap;
  }

  /**
   * Gets key.
   *
   * @param sldStudentProgramEntity the sld student entity
   * @return the key
   */
  private String getKey(final SldStudentProgramEntity sldStudentProgramEntity) {
    return sldStudentProgramEntity.getSldStudentProgramId().getDistNo() + sldStudentProgramEntity.getSldStudentProgramId().getSchlNo() + sldStudentProgramEntity.getSldStudentProgramId().getReportDate() + sldStudentProgramEntity.getSldStudentProgramId().getEnrolledProgramCode();
  }

  /**
   * Create update statement for each record string.
   *
   * @param updatedPen    the updated pen
   * @param mergedFromPen the merged from pen
   * @return the string
   */
  private String createUpdateStatementForEachRecord(final String updatedPen, final SldStudentProgramEntity mergedFromPen) {
    val builder = new StringBuilder();
    builder
      .append("UPDATE STUDENT_PROGRAMS SET PEN='") // end with beginning single quote
      .append(updatedPen)
      .append("'") // end single quote
      .append(" WHERE ") // starts and ends with a space for valid sql statement
      .append("PEN='") // end with beginning single quote
      .append(mergedFromPen.getSldStudentProgramId().getPen())
      .append("'") // end single quote
      .append(" AND DISTNO='")// end with beginning single quote
      .append(mergedFromPen.getSldStudentProgramId().getDistNo())
      .append("'") // end single quote
      .append(" AND SCHLNO='")
      .append(mergedFromPen.getSldStudentProgramId().getSchlNo())
      .append("'") // end single quote
      .append(" AND REPORT_DATE=") // does not have single quote since it is a numeric field.
      .append(mergedFromPen.getSldStudentProgramId().getReportDate())
      .append(" AND ENROLLED_PROGRAM_CODE='")
      .append(mergedFromPen.getSldStudentProgramId().getEnrolledProgramCode())
      .append("'"); // end single quote
    return builder.toString();
  }
}
