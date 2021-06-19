package ca.bc.gov.educ.api.sld.service;

import ca.bc.gov.educ.api.sld.model.SldStudentEntity;
import ca.bc.gov.educ.api.sld.model.SldStudentId;
import ca.bc.gov.educ.api.sld.repository.SldRepository;
import ca.bc.gov.educ.api.sld.struct.v1.SldStudent;
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

import static ca.bc.gov.educ.api.sld.jooq.Tables.STUDENT;

/**
 * The type Sld student service.
 */
@Service
@Slf4j
public class SldStudentService extends SldBaseService {


  /**
   * The Sld repository.
   */
  @Getter(AccessLevel.PRIVATE)
  private final SldRepository sldRepository;

  /**
   * Instantiates a new Sld student service.
   *
   * @param emf           the emf
   * @param sldRepository the sld repository
   * @param create        the create
   */
  @Autowired
  public SldStudentService(final EntityManagerFactory emf, final SldRepository sldRepository, final DSLContext create) {
    super(emf, create, STUDENT, STUDENT.PEN);
    this.sldRepository = sldRepository;
    this.populateDuplicatePenSuffix();
  }

  /**
   * Populate duplicate pen suffix.
   */


  /**
   * Gets sld by pen.
   *
   * @param pen the pen
   * @return the sld by pen
   */
  public List<SldStudentEntity> getSldByPen(final String pen) {
    return this.findExistingStudentsByPen(pen);
  }


  /**
   * Update sld students by pen list.
   *
   * @param pen        the pen
   * @param sldStudent the sld student
   * @return the list
   */
  public List<SldStudentEntity> updateSldStudentsByPen(final String pen, final SldStudent sldStudent) {
    final List<SldStudentEntity> mergedFromPenData = this.findExistingStudentsByPen(pen);
    final List<SldStudentEntity> mergedToPenData = this.findExistingStudentsByPen(sldStudent.getPen());
    final List<String> updateStatements = this.prepareUpdateStatement(mergedFromPenData, mergedToPenData, sldStudent.getPen());
    final int count = this.bulkUpdate(updateStatements);
    if (count > 0) {
      return this.findExistingStudentsByPen(pen.equals(sldStudent.getPen()) ? pen : sldStudent.getPen());
    } else {
      return List.of();
    }
  }

  /**
   * Prepare update statement list.
   * **
   * * Using the STUDENT table as an example, here is how the 'tie breaker' is
   * * assigned to a PEN within this program:
   * * Student is opened as STUDENT_OLD for read/update and STUDENT_NEW for read
   * * only.
   * * Read (loop) STUDENT_OLD using ('old PEN'[1:9] + "@") in the order of
   * * DISTNO, SCHLNO, REPORT_DATE. For any existing STUDENT_OLD record:
   * * If DISTNO, SCHLNO or REPORT_DATE are different from the previous
   * * STUDENT_OLD record (if any) for this PEN then:
   * * Read (loop) STUDENT_NEW using the DISTNO, SCHLNO, REPORT_DATE of
   * * STUDENT_OLD and ('new PEN'[1:9] + "@"), in order to find out if
   * * 'new PEN' already exists for the same school and report date and if so,
   * * what the highest value of the tie-breaker' is (if any) for 'new PEN'.
   * * Most of the time, 'new PEN' will not exist for the same DISTNO, SCHLNO
   * * and REPORT_DATE or else the school would have reported the same student
   * * under two different PENs in the same 1701 collection.
   * * The 'old PEN' in STUDENT_OLD is changed to 'new PEN'[1:9] + the next
   * * value of 'tie breaker' that is greater than the highest value found
   * * above. The new value of tie-breaker is saved as the highest value and
   * * the looped read of STUDENT_OLD continues.
   * *
   * **
   *
   * @param mergedFromPenData the merged from pen data
   * @param mergedToPenData   the merged to pen data
   * @param mergedToPen       the merged to pen
   * @return the list
   */
  private List<String> prepareUpdateStatement(final List<SldStudentEntity> mergedFromPenData, final List<SldStudentEntity> mergedToPenData, final String mergedToPen) {
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
   * Create update statement for each record string.
   *
   * @param updatedPen    the updated pen
   * @param mergedFromPen the merged from pen
   * @return the string
   */
  private String createUpdateStatementForEachRecord(final String updatedPen, final SldStudentEntity mergedFromPen) {
    val builder = new StringBuilder();
    builder
      .append("UPDATE STUDENT SET PEN='") // end with beginning single quote
      .append(updatedPen)
      .append("'") // end single quote
      .append(" WHERE ") // starts and ends with a space for valid sql statement
      .append("PEN='") // end with beginning single quote
      .append(mergedFromPen.getSldStudentId().getPen())
      .append("'") // end single quote
      .append(" AND DISTNO='")// end with beginning single quote
      .append(mergedFromPen.getSldStudentId().getDistNo())
      .append("'") // end single quote
      .append(" AND SCHLNO='")
      .append(mergedFromPen.getSldStudentId().getSchlNo())
      .append("'") // end single quote
      .append(" AND REPORT_DATE=") // does not have single quote since it is a numeric field.
      .append(mergedFromPen.getSldStudentId().getReportDate());
    return builder.toString();
  }

  /**
   * Create merge to pen map map.
   *
   * @param mergedToPenData the merged to pen data
   * @return the map
   */
  private Map<String, List<String>> createMergeToPenMap(final List<SldStudentEntity> mergedToPenData) {
    final Map<String, List<String>> penMap = new HashMap<>();
    mergedToPenData.forEach(el -> {
      final String key = this.getKey(el);
      if (penMap.containsKey(key)) {
        val penList = penMap.get(key);
        penList.add(el.getSldStudentId().getPen());
      } else {
        final List<String> penList = new ArrayList<>();
        penList.add(el.getSldStudentId().getPen());
        penMap.put(key, penList);
      }
    });
    return penMap;
  }

  /**
   * Gets key.
   *
   * @param sldStudentEntity the sld student entity
   * @return the key
   */
  private String getKey(final SldStudentEntity sldStudentEntity) {
    return sldStudentEntity.getSldStudentId().getDistNo() + sldStudentEntity.getSldStudentId().getSchlNo() + sldStudentEntity.getSldStudentId().getReportDate();
  }

  /**
   * Find existing students by pen list.
   *
   * @param pen the pen
   * @return the list
   */
  protected List<SldStudentEntity> findExistingStudentsByPen(final String pen) {
    final ExampleMatcher studentMatcher = ExampleMatcher.matchingAny()
      .withStringMatcher(ExampleMatcher.StringMatcher.STARTING);
    return this.getSldRepository().findAll(Example.of(SldStudentEntity.builder().sldStudentId(SldStudentId.builder().pen(pen).build()).build(), studentMatcher));
  }

}
