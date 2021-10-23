package ca.bc.gov.educ.api.sld.constant;

/**
 * The enum Event type.
 */
public enum EventType {
  /**
   * Update sld students event type.
   */
  UPDATE_SLD_STUDENTS,
  /**
   * Update sld students by ids event type.
   */
  UPDATE_SLD_STUDENTS_BY_IDS,
  /**
  * Update sld dia students event type.
  */
  UPDATE_SLD_DIA_STUDENTS,
  /**
   * Update sld student programs event type.
   */
  UPDATE_SLD_STUDENT_PROGRAMS,
  /**
   * Update sld student programs by data event type.
   */
  UPDATE_SLD_STUDENT_PROGRAMS_BY_DATA,
  /**
   * Restore sld students event type.
   */
  RESTORE_SLD_STUDENTS,
  /**
   * Restore sld dia students event type.
   */
  RESTORE_SLD_DIA_STUDENTS,
  /**
   * Restore sld student programs event type.
   */
  RESTORE_SLD_STUDENT_PROGRAMS,
  /**
   * SLD event outbox processed event type.
   */
  SLD_EVENT_OUTBOX_PROCESSED
}
