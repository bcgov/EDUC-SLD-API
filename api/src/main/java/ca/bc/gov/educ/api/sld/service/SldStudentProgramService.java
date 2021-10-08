package ca.bc.gov.educ.api.sld.service;

import ca.bc.gov.educ.api.sld.constant.EntityName;
import ca.bc.gov.educ.api.sld.model.SldStudentProgramEntity;
import ca.bc.gov.educ.api.sld.model.SldStudentProgramId;
import ca.bc.gov.educ.api.sld.repository.SldStudentProgramRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;

/**
 * The type sld student program service.
 */
@Service
@Slf4j
public class SldStudentProgramService extends SldBaseService<SldStudentProgramId, SldStudentProgramEntity> {

  @Getter(AccessLevel.PRIVATE)
  private final SldStudentProgramRepository sldStudentProgramRepository;

  /**
   * Instantiates a new sld student program service.
   *
   * @param emf                         the EntityManagerFactory
   * @param sldStudentProgramRepository the sld student program repo
   */
  @Autowired
  public SldStudentProgramService(final EntityManagerFactory emf, final SldStudentProgramRepository sldStudentProgramRepository) {
    super(emf);
    this.sldStudentProgramRepository = sldStudentProgramRepository;
  }


  @Override
  protected List<SldStudentProgramEntity> findExistingDataByPen(final String pen) {
    return this.findExistingSLDStudentProgramsByPen(pen);
  }

  @Override
  protected Optional<SldStudentProgramEntity> findExistingDataById(final SldStudentProgramId id) {
    return this.getSldStudentProgramRepository().findById(id);
  }

  @Override
  protected List<SldStudentProgramEntity> findExistingDataByIds(List<SldStudentProgramId> ids) {
    return this.getSldStudentProgramRepository().findAllById(ids);
  }

  @Override
  protected List<SldStudentProgramEntity> findExistingDataByDataMatcher(final SldStudentProgramEntity sldStudentProgramEntity) {
    return this.findExistingStudentsByDataMatcher(sldStudentProgramEntity);
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

  protected List<SldStudentProgramEntity> findExistingStudentsByDataMatcher(final SldStudentProgramEntity sldStudentProgramEntity) {
    final ExampleMatcher studentMatcher = ExampleMatcher.matchingAll()
      .withStringMatcher(ExampleMatcher.StringMatcher.STARTING);
    return this.getSldStudentProgramRepository().findAll(Example.of(sldStudentProgramEntity, studentMatcher));
  }


  /**
   * Gets key.
   *
   * @param sldStudentProgramEntity the sld student entity
   * @return the key
   */
  @Override
  protected String getKey(final SldStudentProgramEntity sldStudentProgramEntity) {
    return StringUtils.trimToEmpty(sldStudentProgramEntity.getSldStudentProgramId().getDistNo()) + StringUtils.trimToEmpty(sldStudentProgramEntity.getSldStudentProgramId().getSchlNo()) + sldStudentProgramEntity.getSldStudentProgramId().getReportDate() + StringUtils.trimToEmpty(sldStudentProgramEntity.getSldStudentProgramId().getEnrolledProgramCode());
  }

  @Override
  protected String getPen(final SldStudentProgramEntity sldStudentProgramEntity) {
    return StringUtils.trim(sldStudentProgramEntity.getSldStudentProgramId().getPen());
  }

  @Override
  protected SldStudentProgramId getNewId(final SldStudentProgramEntity sldStudentProgramEntity, final String updatedPen) {
    final var id = SerializationUtils.clone(sldStudentProgramEntity.getSldStudentProgramId());
    id.setPen(updatedPen);
    return id;
  }

  /**
   * Create update statement for each record string.
   *
   * @param updatedPen    the updated pen
   * @param mergedFromPen the merged from pen
   * @return the string
   */
  @Override
  protected String createUpdateStatementForEachRecord(final String updatedPen, final SldStudentProgramEntity mergedFromPen) {
    val builder = new StringBuilder();
    builder
      .append("UPDATE STUDENT_PROGRAMS SET PEN='") // end with beginning single quote
      .append(updatedPen)
      .append("'"); // end single quote

    //if mergedFromPen has already been merged and is not moved back, set student_id to the recently merged pen value to handle the merge chain issue.
    if(!StringUtils.equals(mergedFromPen.getStudentId(), mergedFromPen.getSldStudentProgramId().getPen()) && !isSimilarPen(mergedFromPen.getStudentId(), updatedPen)) {
      builder
        .append(", STUDENT_ID='") // end with beginning single quote
        .append(mergedFromPen.getSldStudentProgramId().getPen())
        .append("'"); // end single quote
    }

    builder
      .append(" WHERE ") // starts and ends with a space for valid sql statement
      .append("PEN='") // end with beginning single quote
      .append(mergedFromPen.getSldStudentProgramId().getPen())
      .append("'") // end single quote
      .append(" AND DISTNO='")// end with beginning single quote
      .append(StringUtils.trimToEmpty(mergedFromPen.getSldStudentProgramId().getDistNo()))
      .append("'") // end single quote
      .append(" AND SCHLNO='")
      .append(StringUtils.trimToEmpty(mergedFromPen.getSldStudentProgramId().getSchlNo()))
      .append("'") // end single quote
      .append(" AND REPORT_DATE=") // does not have single quote since it is a numeric field.
      .append(mergedFromPen.getSldStudentProgramId().getReportDate())
      .append(" AND ENROLLED_PROGRAM_CODE='")
      .append(StringUtils.trimToEmpty(mergedFromPen.getSldStudentProgramId().getEnrolledProgramCode()))
      .append("'"); // end single quote
    return builder.toString();
  }

  /**
   * Create restore statement for each pen.
   *
   * @param mergedFromPen the merged from pen
   * @return the string
   */
  @Override
  protected String createRestoreStatementForEachPen(final String updatedPen, final String mergedFromPen) {
    val builder = new StringBuilder();
    builder
      .append("UPDATE STUDENT_PROGRAMS SET PEN=STUDENT_ID")
      .append(" WHERE ") // starts and ends with a space for valid sql statement
      .append("PEN like '") // end with beginning single quote
      .append(updatedPen)
      .append("%'") // end single quote
      .append(" AND STUDENT_ID like '")// end with beginning single quote
      .append(mergedFromPen)
      .append("%'"); // end single quote
    return builder.toString();
  }

  @Override
  public Optional<SldStudentProgramEntity> update(final SldStudentProgramId id, final SldStudentProgramEntity sldStudentProgramEntity) {
    return super.update(id, sldStudentProgramEntity.getSldStudentProgramId().getPen());
  }

  @Override
  public List<SldStudentProgramEntity> updateBatch(final SldStudentProgramEntity originalEntity, final SldStudentProgramEntity sldStudentProgramEntity) {
    return super.updateBatch(originalEntity, sldStudentProgramEntity.getSldStudentProgramId().getPen());
  }

  @Override
  public List<SldStudentProgramEntity> restore(final String pen, final SldStudentProgramEntity sldStudentProgramEntity) {
    return super.restore(pen, sldStudentProgramEntity.getSldStudentProgramId().getPen());
  }

  @Override
  public EntityName getEntityName() {
    return EntityName.STUDENT_PROGRAMS;
  }

}
