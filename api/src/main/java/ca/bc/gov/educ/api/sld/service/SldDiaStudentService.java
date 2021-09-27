package ca.bc.gov.educ.api.sld.service;

import ca.bc.gov.educ.api.sld.constant.EntityName;
import ca.bc.gov.educ.api.sld.exception.SldRuntimeException;
import ca.bc.gov.educ.api.sld.model.SldDiaStudentEntity;
import ca.bc.gov.educ.api.sld.model.SldDiaStudentId;
import ca.bc.gov.educ.api.sld.repository.SldDiaStudentRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import java.util.List;

/**
 * The type sld dia student service.
 */
@Service
@Slf4j
public class SldDiaStudentService extends SldBaseService<SldDiaStudentEntity> {

  @Getter(AccessLevel.PRIVATE)
  private final SldDiaStudentRepository sldDiaStudentRepository;

  /**
   * Instantiates a new sld student service.
   *
   * @param emf                     the EntityManagerFactory
   * @param sldDiaStudentRepository the sld dia student repo
   */
  @Autowired
  public SldDiaStudentService(final EntityManagerFactory emf, final SldDiaStudentRepository sldDiaStudentRepository) {
    super(emf);
    this.sldDiaStudentRepository = sldDiaStudentRepository;
  }


  @Override
  protected List<SldDiaStudentEntity> findExistingDataByPen(final String pen) {
    return this.findExistingStudentsByPen(pen);
  }

  @Override
  protected String createUpdateStatementForEachRecord(final String updatedPen, final SldDiaStudentEntity sldDiaStudentEntity) {
    val builder = new StringBuilder();
    builder
      .append("UPDATE DIA_STUDENT SET PEN='") // end with beginning single quote
      .append(updatedPen)
      .append("'") // end single quote
      .append(" WHERE ") // starts and ends with a space for valid sql statement
      .append("PEN='") // end with beginning single quote
      .append(sldDiaStudentEntity.getSldDiaStudentId().getPen())
      .append("'") // end single quote
      .append(" AND DISTNO='")// end with beginning single quote
      .append(StringUtils.trimToEmpty(sldDiaStudentEntity.getDistNo()))
      .append("'") // end single quote
      .append(" AND SCHLNO='")
      .append(StringUtils.trimToEmpty(sldDiaStudentEntity.getSchlNo()))
      .append("'") // end single quote
      .append(" AND REPORT_DATE=") // does not have single quote since it is a numeric field.
      .append(sldDiaStudentEntity.getSldDiaStudentId().getReportDate())
      .append(" AND RECORD_NUMBER=") // does not have single quote since it is a numeric field.
      .append(sldDiaStudentEntity.getRecordNumber());
    return builder.toString();
  }

  /**
   * Create restore statement for each pen.
   *
   * @param mergedFromPen the merged from pen
   * @return the string
   */
  @Override
  protected String createRestoreStatementForEachPen(final SldDiaStudentEntity mergedFromPen) {
    throw new SldRuntimeException("Don't support restore operation.");
  }

  @Override
  protected String getKey(final SldDiaStudentEntity sldDiaStudentEntity) {
    return sldDiaStudentEntity.getSldDiaStudentId().getReportDate() + StringUtils.trimToEmpty(sldDiaStudentEntity.getDistNo()) + StringUtils.trimToEmpty(sldDiaStudentEntity.getSchlNo()) + sldDiaStudentEntity.getRecordNumber();
  }

  @Override
  protected String getPen(final SldDiaStudentEntity sldDiaStudentEntity) {
    return StringUtils.trim(sldDiaStudentEntity.getSldDiaStudentId().getPen());
  }

  /**
   * Find existing students by pen list.
   *
   * @param pen the pen
   * @return the list
   */

  protected List<SldDiaStudentEntity> findExistingStudentsByPen(final String pen) {
    final ExampleMatcher studentMatcher = ExampleMatcher.matchingAny()
      .withStringMatcher(ExampleMatcher.StringMatcher.STARTING);
    return this.getSldDiaStudentRepository().findAll(Example.of(SldDiaStudentEntity.builder().sldDiaStudentId(SldDiaStudentId.builder().pen(pen).build()).build(), studentMatcher));
  }

  @Override
  public List<SldDiaStudentEntity> update(final String pen, final SldDiaStudentEntity sldDiaStudentEntity) {
    return super.update(pen, sldDiaStudentEntity.getSldDiaStudentId().getPen());
  }

  @Override
  public EntityName getEntityName() {
    return EntityName.DIA_STUDENT;
  }

  @Override
  protected List<SldDiaStudentEntity> findExistingDataByStudentId(final String studentId) {
    throw new SldRuntimeException("Don't support StudentId field.");
  }
}
