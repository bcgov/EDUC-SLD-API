package ca.bc.gov.educ.api.sld.constant;

/**
 * The enum Event type.
 */
public enum EventType {
  /**
   * The Update sld students.
   */
  UPDATE_SLD_STUDENTS,
  /**
   * The Update sld students by ids.
   */
  UPDATE_SLD_STUDENTS_BY_IDS,
  /**
   * The Update sld dia students.
   */
  UPDATE_SLD_DIA_STUDENTS,
  /**
   * The Update sld student programs.
   */
  UPDATE_SLD_STUDENT_PROGRAMS,
  /**
   * The Update sld student programs by data.
   */
  UPDATE_SLD_STUDENT_PROGRAMS_BY_DATA,
  /**
   * The Restore sld students.
   */
  RESTORE_SLD_STUDENTS,
  /**
   * The Restore sld dia students.
   */
  RESTORE_SLD_DIA_STUDENTS,
  /**
   * The Restore sld student programs.
   */
  RESTORE_SLD_STUDENT_PROGRAMS,
  /**
   * The Sld event outbox processed.
   */
  SLD_EVENT_OUTBOX_PROCESSED,
  /**
   * Create sld dia students event type.
   */
  CREATE_SLD_DIA_STUDENTS
}
