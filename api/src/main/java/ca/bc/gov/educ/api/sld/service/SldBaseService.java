package ca.bc.gov.educ.api.sld.service;

import ca.bc.gov.educ.api.sld.exception.SldRuntimeException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import javax.persistence.EntityManagerFactory;
import java.util.*;
import java.util.stream.Collectors;


/**
 * The type sld base service.
 */
@Slf4j
public abstract class SldBaseService<S, T> implements SldService<S, T> {
  /**
   * The Duplicate pen suffix.
   */
  protected static final List<String> duplicatePenSuffix = new ArrayList<>();
  /**
   * The Emf.
   */
  private final EntityManagerFactory emf;


  /**
   * Instantiates a new sld service.
   *
   * @param emf the EntityManagerFactory
   */
  protected SldBaseService(final EntityManagerFactory emf) {
    this.emf = emf;
  }

  protected List<T> updateBatch(final List<T> mergedFromPenData, final String mergedToPen) {
    final List<T> mergedToPenData = this.findExistingDataByPen(mergedToPen);
    final var updateStatements = this.prepareUpdateStatement(mergedFromPenData, mergedToPenData, mergedToPen);
    final int count = this.bulkUpdate(updateStatements.getLeft());
    if (count > 0) {
      return this.findExistingDataByIds(updateStatements.getRight());
    } else {
      return List.of();
    }
  }

  protected List<T> updateBatchByExample(final T mergedFromData, final String mergedToPen) {
    final List<T> mergedFromPenData = this.findExistingDataByDataMatcher(mergedFromData);
    return this.updateBatch(mergedFromPenData, mergedToPen);
  }

  protected List<T> updateBatchByIds(final List<S> mergedFromIds, final String mergedToPen) {
    final List<T> mergedFromPenData = this.findExistingDataByIds(mergedFromIds);
    return this.updateBatch(mergedFromPenData, mergedToPen);
  }

  protected List<T> updateBatchByExamples(final List<T> mergedFromData, final String mergedToPen) {
    final List<T> mergedFromPenData = mergedFromData.stream().flatMap(data -> this.findExistingDataByDataMatcher(data).stream()).collect(Collectors.toList());
    return this.updateBatch(mergedFromPenData, mergedToPen);
  }

  protected Optional<T> update(final S mergedFromId, final String mergedToPen) {
    return this.findExistingDataById(mergedFromId).flatMap((mergedFromPenData) -> {
      final List<T> mergedToPenData = this.findExistingDataByPen(mergedToPen);
      final var updateStatements = this.prepareUpdateStatement(List.of(mergedFromPenData), mergedToPenData, mergedToPen);
      final int count = this.bulkUpdate(updateStatements.getLeft());
      if (count > 0) {
        return this.findExistingDataById(updateStatements.getRight().get(0));
      } else {
        return Optional.empty();
      }
    });
  }

  public List<T> restore(final String pen, final String mergedToPen) {
    final List<String> updateStatements = this.prepareRestoreStatement(mergedToPen, pen);
    final int count = this.bulkUpdate(updateStatements);
    if (count > 0) {
      return this.findExistingDataByPen(pen);
    } else {
      return List.of();
    }
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
   * <pre>
   *   The Above old logic is written using Maps to identify duplicate rows,
   *   child classes implements abstract methods to provide their own behavior.
   *
   * </pre>
   *
   * @param mergedFromPenData the merged from pen data
   * @param mergedToPenData   the merged to pen data
   * @param mergedToPen       the merged to pen
   * @return the pair of lists, left list is update statements, right list is new ids
   */

  protected Pair<List<String>,List<S>>  prepareUpdateStatement(final List<T> mergedFromPenData, final List<T> mergedToPenData, final String mergedToPen) {
    final List<String> updateStatements = new ArrayList<>();
    final List<S> newIds = new ArrayList<>();
    final Map<String, List<String>> mergeToPenMap = this.createMergeToPenMap(mergedToPenData);
    final Map<String, List<String>> mergeFromPenMap = this.createMergeToPenMap(mergedFromPenData);
    for (val mergedFromPen : mergedFromPenData) {
      val key = this.getKey(mergedFromPen);
      Optional<String> highestPenOptional = Optional.empty();
      if (mergeFromPenMap.containsKey(key) && mergeToPenMap.containsKey(key)) { // if both have same rows find who has the largest and add the 10th char accordingly.
        final String highestPen = this.getHighestPenFromBothDirection(mergedToPen, mergeToPenMap, mergeFromPenMap, key);
        highestPenOptional = this.computeNextPen(highestPen);
        mergeFromPenMap.get(key).add(highestPenOptional.orElseThrow());
        mergeToPenMap.get(key).add(highestPenOptional.orElseThrow());
      } else if (mergeToPenMap.containsKey(key)) {// if only merge to pen has duplicates then find the largest and add the 10th char accordingly.
        final String highestPen = this.findHighestPen(mergeToPenMap, key);
        highestPenOptional = this.computeNextPen(highestPen);
        mergeToPenMap.get(key).add(highestPenOptional.orElseThrow());
      } else if (StringUtils.length(this.getPen(mergedFromPen)) == 10) { // check if the merge from PEN is a duplicate one and update the true pen accordingly
        highestPenOptional = Optional.of(mergedToPen + StringUtils.substring(this.getPen(mergedFromPen), 9, 10));
        List<String> pens = mergeToPenMap.get(key);
        if (pens == null) {
          pens = new ArrayList<>();
        }
        pens.add(highestPenOptional.orElseThrow());
      }
      final String updatedPen = highestPenOptional.orElse(mergedToPen);
      updateStatements.add(this.createUpdateStatementForEachRecord(updatedPen, mergedFromPen));
      newIds.add(this.getNewId(mergedFromPen, updatedPen));
    }
    return Pair.of(updateStatements, newIds);
  }

  protected List<String> prepareRestoreStatement(final String mergedToPen, final String mergedFromPen) {
    return List.of(this.createRestoreStatementForEachPen(mergedToPen, mergedFromPen));
  }

  protected boolean isSimilarPen(final String penA, final String penB) {
    final var realPenA = StringUtils.length(penA) >= 10 ? StringUtils.substring(penA, 0, 9) : penA;
    final var realPenB = StringUtils.length(penB) >= 10 ? StringUtils.substring(penB, 0, 9) : penB;
    return StringUtils.equals(realPenA, realPenB);
  }

  private String getHighestPenFromBothDirection(final String mergedToPen, final Map<String, List<String>> mergeToPenMap, final Map<String, List<String>> mergeFromPenMap, final String key) {
    val highestToPen = StringUtils.trim(this.findHighestPen(mergeToPenMap, key));
    val highestFromPen = StringUtils.trim(this.findHighestPen(mergeFromPenMap, key));
    final String highestPen;
    if (highestFromPen.length() == 10 && highestToPen.length() == 10) { // compare only the last character
      final String tempPen = StringUtils.compare(highestFromPen.substring(9, 10), highestToPen.substring(9, 10)) > 0 ? highestFromPen : highestToPen;
      highestPen = mergedToPen + tempPen.charAt(9);
    } else if (highestFromPen.trim().length() == 10) {
      highestPen = mergedToPen + highestFromPen.charAt(9);
    } else if (highestToPen.trim().length() == 10) {
      highestPen = highestToPen.trim();
    } else {
      highestPen = mergedToPen;
    }
    return highestPen;
  }

  private Optional<String> computeNextPen(final String highestPen) {
    final Optional<String> highestPenOptional;
    final String nextPen;
    if (highestPen.trim().length() == 10) { // if it is 10 characters it already has a duplicate record.
      val lastCharacter = StringUtils.substring(highestPen, 9, 10);
      val index = duplicatePenSuffix.indexOf(lastCharacter);
      nextPen = StringUtils.substring(highestPen, 0, 9).concat(duplicatePenSuffix.get(index + 1)); // get the first 9 characters then append the next alphabet for the duplicate entry.

    } else {
      nextPen = highestPen.trim().concat("D"); // first duplicate, starts with D
    }
    highestPenOptional = Optional.of(nextPen);
    return highestPenOptional;
  }

  private String findHighestPen(final Map<String, List<String>> penMap, final String key) {
    final List<String> penList = penMap.get(key).stream()
      .sorted(Comparator.reverseOrder())
      .collect(Collectors.toList());
    return penList.isEmpty() ? "" : penList.get(0);
  }


  protected Map<String, List<String>> createMergeToPenMap(final List<T> mergedToPenData) {
    final Map<String, List<String>> penMap = new HashMap<>();
    mergedToPenData.forEach(el -> {
      final String key = this.getKey(el);
      if (penMap.containsKey(key)) {
        val penList = penMap.get(key);
        penList.add(StringUtils.trim(this.getPen(el)));
      } else {
        final List<String> penList = new ArrayList<>();
        penList.add(StringUtils.trim(this.getPen(el)));
        penMap.put(key, penList);
      }
    });
    return penMap;
  }

  // below methods are child specific implementation.
  protected abstract String getPen(T t);

  protected abstract S getNewId(T t, String updatedPen);

  protected abstract String createUpdateStatementForEachRecord(String updatedPen, T mergedFromPen);

  protected abstract String createRestoreStatementForEachPen(String updatedPen, String mergedFromPen);

  protected abstract String getKey(T mergedFromPen);

  protected abstract List<T> findExistingDataByPen(String pen);

  protected abstract Optional<T> findExistingDataById(S id);

  protected abstract List<T> findExistingDataByIds(List<S> ids);

  protected abstract List<T> findExistingDataByDataMatcher(T data);
}
