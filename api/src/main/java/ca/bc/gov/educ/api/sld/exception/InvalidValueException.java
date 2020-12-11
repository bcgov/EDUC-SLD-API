package ca.bc.gov.educ.api.sld.exception;

import java.util.Map;

/**
 * InvalidValueException to provide error details when invalid value of a
 * parameter is passed to endpoint
 */
public class InvalidValueException extends RuntimeException {

    private static final long serialVersionUID = 8926815015510650437L;

  /**
   * Instantiates a new Invalid value exception.
   *
   * @param paramsMap the params map
   */
  public InvalidValueException(String... paramsMap) {
        super(InvalidValueException.generateMessage(
            ExceptionUtils.toMap(String.class, String.class, (Object[]) paramsMap))) ;
    }

    private static String generateMessage(Map<String, String> values) {
        String message = "Invalid request parameters provided: ";
        return message + values;
    }
}
