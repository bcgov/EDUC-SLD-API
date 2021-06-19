package ca.bc.gov.educ.api.sld.service;

import ca.bc.gov.educ.api.sld.exception.SldRuntimeException;
import ca.bc.gov.educ.api.sld.util.BeanUtil;
import com.google.common.base.CaseFormat;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jooq.*;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;


/**
 * The type sld base service.
 */
@Slf4j
public abstract class SldBaseService {
  /**
   * The Duplicate pen suffix.
   */
  protected static final List<String> duplicatePenSuffix = new ArrayList<>();
  /**
   * The Emf.
   */
  private final EntityManagerFactory emf;

  private final DSLContext create;

  private final TableField<?, String> penField;

  private final Table<?> table;

  /**
   * Instantiates a new sld service.
   *
   * @param emf    the EntityManagerFactory
   * @param create the DSLContext
   */
  protected SldBaseService(final EntityManagerFactory emf, final DSLContext create, final Table<?> table, final TableField<?, String> penField) {
    this.emf = emf;
    this.create = create;
    this.table = table;
    this.penField = penField;
  }

  /**
   * Update sld data by pen.
   *
   * @param pen     the PEN
   * @param sldData the Sld Student data
   * @return the SldStudentEntity list
   */
  public <T> int updateSldDataByPen(final String pen, final T sldData) {
    val em = this.emf.createEntityManager();

    val tx = em.getTransaction();

    int rowsUpdated;

    // below timeout is in milli seconds, so it is 10 seconds.
    try {
      final var jooqQuery = this.buildUpdate(pen, sldData);
      tx.begin();
      val sql = jooqQuery.getSQL();
      log.info("generated sql is :: {}", sql);
      final var nativeQuery = em.createNativeQuery(sql).setHint("javax.persistence.query.timeout", 10000);
      final var values = jooqQuery.getBindValues();
      IntStream.range(0, values.size()).forEach(index -> nativeQuery.setParameter(index + 1, values.get(index)));
      rowsUpdated = nativeQuery.executeUpdate();
      tx.commit();
    } catch (final Exception e) {
      log.error("Error occurred saving entity " + e.getMessage());
      tx.rollback();
      throw new SldRuntimeException("Error occurred saving entity", e);
    } finally {
      if (em.isOpen()) {
        em.close();
      }
    }

    return rowsUpdated;
  }

  /**
   * Build update string.
   *
   * @param pen     the PEN
   * @param sldData the new SLD data
   * @return the jooq Query
   */
  private <T> Query buildUpdate(final String pen, final T sldData) {
    final Record sldRecord = this.create.newRecord(this.table);

    BeanUtil.getFields(sldData).forEach(field -> {
      try {
        final var fieldValue = BeanUtil.getFieldValue(field, sldData);
        if (fieldValue != null) {
          var propertyName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, field.getName());
          if (field.getName().equals("distNo")) {
            propertyName = "DISTNO";
          } else if (field.getName().equals("schlNo")) {
            propertyName = "SCHLNO";
          }

          final var tableField = (Field<Object>) this.table.getClass().getField(propertyName).get(this.table);
          sldRecord.set(tableField, fieldValue);
        }
      } catch (final IllegalAccessException | NoSuchFieldException e) {
        throw new SldRuntimeException("Failed to build update sql", e);
      }
    });

    return this.create.update(this.table).set(sldRecord)
      .where(this.penField.eq(pen));
  }

  /**
   * pass all the native update statements and everything will be committed as part of single transaction.
   *
   * @param updateStatements the list of update statements to be executed.
   */
  protected int bulkUpdate(final List<String> updateStatements) {
    val em = this.emf.createEntityManager();

    val tx = em.getTransaction();

    var rowsUpdated = 0;
    // below timeout is in milli seconds, so it is 10 seconds.
    try {
      tx.begin();
      for (val updateStatement : updateStatements) {
        log.info("generated sql is :: {}", updateStatement);
        final var nativeQuery = em.createNativeQuery(updateStatement).setHint("javax.persistence.query.timeout", 10000);
        rowsUpdated += nativeQuery.executeUpdate();
      }
      tx.commit();
    } catch (final Exception e) {
      log.error("Error occurred saving entity " + e.getMessage());
      tx.rollback();
      throw new SldRuntimeException("Error occurred saving entity", e);
    } finally {
      if (em.isOpen()) {
        em.close();
      }
    }
    return rowsUpdated;
  }

  protected synchronized void populateDuplicatePenSuffix() {
    if (duplicatePenSuffix.isEmpty()) {
      duplicatePenSuffix.add("A");
      duplicatePenSuffix.add("B");
      duplicatePenSuffix.add("C");
      duplicatePenSuffix.add("D");
      duplicatePenSuffix.add("E");
      duplicatePenSuffix.add("F");
      duplicatePenSuffix.add("G");
      duplicatePenSuffix.add("H");
      duplicatePenSuffix.add("I");
      duplicatePenSuffix.add("J");
      duplicatePenSuffix.add("K");
      duplicatePenSuffix.add("L");
      duplicatePenSuffix.add("M");
      duplicatePenSuffix.add("N");
      duplicatePenSuffix.add("O");
      duplicatePenSuffix.add("P");
      duplicatePenSuffix.add("Q");
      duplicatePenSuffix.add("R");
      duplicatePenSuffix.add("S");
      duplicatePenSuffix.add("T");
      duplicatePenSuffix.add("U");
      duplicatePenSuffix.add("V");
      duplicatePenSuffix.add("W");
      duplicatePenSuffix.add("X");
      duplicatePenSuffix.add("Y");
      duplicatePenSuffix.add("Z");
    }
  }
}
