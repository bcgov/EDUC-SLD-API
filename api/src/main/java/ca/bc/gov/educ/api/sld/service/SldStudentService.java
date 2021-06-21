package ca.bc.gov.educ.api.sld.service;

import ca.bc.gov.educ.api.sld.constant.EntityName;
import ca.bc.gov.educ.api.sld.model.SldStudentEntity;
import ca.bc.gov.educ.api.sld.model.SldStudentId;
import ca.bc.gov.educ.api.sld.repository.SldRepository;
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
 * The type Sld student service.
 */
@Service
@Slf4j
public class SldStudentService extends SldBaseService<SldStudentEntity> {


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
   */
  @Autowired
  public SldStudentService(final EntityManagerFactory emf, final SldRepository sldRepository) {
    super(emf);
    this.sldRepository = sldRepository;
    this.populateDuplicatePenSuffix();
  }


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
   * Create update statement for each record string.
   *
   * @param updatedPen    the updated pen
   * @param mergedFromPen the merged from pen
   * @return the string
   */
  @Override
  protected String createUpdateStatementForEachRecord(final String updatedPen, final SldStudentEntity mergedFromPen) {
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
      .append(StringUtils.trimToEmpty(mergedFromPen.getSldStudentId().getDistNo()))
      .append("'") // end single quote
      .append(" AND SCHLNO='")
      .append(StringUtils.trimToEmpty(mergedFromPen.getSldStudentId().getSchlNo()))
      .append("'") // end single quote
      .append(" AND REPORT_DATE=") // does not have single quote since it is a numeric field.
      .append(mergedFromPen.getSldStudentId().getReportDate());
    return builder.toString();
  }


  /**
   * Gets key.
   *
   * @param sldStudentEntity the sld student entity
   * @return the key
   */
  @Override
  protected String getKey(final SldStudentEntity sldStudentEntity) {
    return StringUtils.trimToEmpty(sldStudentEntity.getSldStudentId().getDistNo()) + StringUtils.trimToEmpty(sldStudentEntity.getSldStudentId().getSchlNo()) + sldStudentEntity.getSldStudentId().getReportDate();
  }

  @Override
  protected List<SldStudentEntity> findExistingDataByPen(final String pen) {
    return this.findExistingStudentsByPen(pen);
  }

  @Override
  protected String getPen(final SldStudentEntity sldStudentEntity) {
    return sldStudentEntity.getSldStudentId().getPen();
  }

  protected List<SldStudentEntity> findExistingStudentsByPen(final String pen) {
    final ExampleMatcher studentMatcher = ExampleMatcher.matchingAny()
      .withStringMatcher(ExampleMatcher.StringMatcher.STARTING);
    return this.getSldRepository().findAll(Example.of(SldStudentEntity.builder().sldStudentId(SldStudentId.builder().pen(pen).build()).build(), studentMatcher));
  }

  @Override
  public List<SldStudentEntity> update(final String pen, final SldStudentEntity sldStudentEntity) {
    return super.update(pen, sldStudentEntity.getSldStudentId().getPen());
  }

  @Override
  public EntityName getEntityName() {
    return EntityName.STUDENT;
  }
}
