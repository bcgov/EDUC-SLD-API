package ca.bc.gov.educ.api.sld.exception;

/**
 * The type SLD runtime exception.
 */
public class SldRuntimeException extends RuntimeException {

  private static final long serialVersionUID = 5241655513745148898L;

  /**
   * Instantiates a new SLD runtime exception.
   *
   * @param message the message
   */
  public SldRuntimeException(String message) {
    super(message);
  }

  /**
   * Instantiates a new SLD runtime exception.
   *
   * @param message the message
   * @param cause the cause of the exception
   */
  public SldRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }

}
