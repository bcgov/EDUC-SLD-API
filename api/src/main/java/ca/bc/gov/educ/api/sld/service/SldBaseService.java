package ca.bc.gov.educ.api.sld.service;

import ca.bc.gov.educ.api.sld.exception.SldRuntimeException;
import ca.bc.gov.educ.api.sld.util.BeanUtil;
import com.google.common.base.CaseFormat;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jooq.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.stream.IntStream;


/**
 * The type sld base service.
 */
@Slf4j
public abstract class SldBaseService {

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
   * @param emf the EntityManagerFactory
   * @param create the DSLContext
   */
  public SldBaseService(final EntityManagerFactory emf, final DSLContext create, final Table<?> table, final TableField<?, String> penField) {
    this.emf = emf;
    this.create = create;
    this.table = table;
    this.penField = penField;
  }

  /**
   * Update sld data by pen.
   *
   * @param pen the PEN
   * @param sldData the Sld Student data
   * @return the SldStudentEntity list
   */
  public <T> int updateSldDataByPen(String pen, T sldData) {
    EntityManager em = this.emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();

    int rowsUpdated;

    // below timeout is in milli seconds, so it is 10 seconds.
    try {
      var jooqQuery = buildUpdate(pen, sldData);
      tx.begin();
      val sql = jooqQuery.getSQL();
      log.info("generated sql is :: {}", sql);
      var nativeQuery = em.createNativeQuery(sql).setHint("javax.persistence.query.timeout", 10000);
      var values = jooqQuery.getBindValues();
      IntStream.range(0, values.size()).forEach(index -> nativeQuery.setParameter(index + 1, values.get(index)));
      rowsUpdated = nativeQuery.executeUpdate();
      tx.commit();
    } catch (Exception e) {
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
   * @param pen the PEN
   * @param sldData the new SLD data
   * @return the jooq Query
   */
  private <T> Query buildUpdate(String pen, T sldData) {
    Record record = create.newRecord(table);

    BeanUtil.getFields(sldData).forEach(field -> {
      try {
        var fieldValue = BeanUtil.getFieldValue(field, sldData);
        if(fieldValue != null) {
          var propertyName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, field.getName());
          if(field.getName().equals("distNo")) {
            propertyName = "DISTNO";
          } else if(field.getName().equals("schlNo")) {
            propertyName = "SCHLNO";
          }

          var tableField = (Field<Object>) table.getClass().getField(propertyName).get(table);
          record.set(tableField, fieldValue);
        }
      } catch (IllegalAccessException | NoSuchFieldException e) {
        throw new SldRuntimeException("Failed to build update sql", e);
      }
    });

    return create.update(table).set(record)
      .where(penField.eq(pen));
  }
}
