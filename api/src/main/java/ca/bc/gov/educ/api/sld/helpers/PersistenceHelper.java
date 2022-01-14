package ca.bc.gov.educ.api.sld.helpers;

import ca.bc.gov.educ.api.sld.exception.SldRuntimeException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Slf4j
public final class PersistenceHelper {
  private PersistenceHelper() {
  }

  /**
   * pass all the native update/insert statements and everything will be committed as part of single transaction.
   *
   * @param emf the Entity manager factory.
   * @param statements the list of statements to be executed.
   */
  public static int bulkExecute(final EntityManagerFactory emf, final List<String> statements) {
    val em = emf.createEntityManager();

    val tx = em.getTransaction();

    var rowsExecuted = 0;
    // below timeout is in milli seconds, so it is 10 seconds.
    try {
      tx.begin();
      for (val statement : statements) {
        log.info("generated sql is :: {}", statement);
        final var nativeQuery = em.createNativeQuery(statement).setHint("javax.persistence.query.timeout", 10000);
        rowsExecuted += nativeQuery.executeUpdate();
      }
      tx.commit();
    } catch (final Exception e) {
      log.error("Error occurred saving entity " + e.getMessage());
      throw new SldRuntimeException("Error occurred saving entity", e);
    } finally {
      if (em.isOpen()) {
        em.close();
      }
    }
    return rowsExecuted;
  }
}
