package ca.bc.gov.educ.api.sld.exception;

import ca.bc.gov.educ.api.sld.exception.errors.ApiError;
import org.jboss.logging.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * The type Rest exception handler.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static Logger log = Logger.getLogger(RestExceptionHandler.class);
    
    /**
     * Handles HttpMessageNotReadableException
     *
     * @param ex HttpMessageNotReadableException
     * @param headers http headers
     * @param status http status code
     * @param request the request object
     * @return response
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.warn("handleHttpMessageNotReadable: ", ex);
        String error = "Malformed JSON request";
        log.error("{} ", error, ex);
        return buildResponseEntity(new ApiError(BAD_REQUEST, error, ex));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

  /**
   * Handles EntityNotFoundException. Created to encapsulate errors with more detail than javax.persistence.EntityNotFoundException.
   *
   * @param ex the EntityNotFoundException
   * @return the ApiError object
   */
  @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(
            EntityNotFoundException ex) {
        log.info("handleEntityNotFound", ex);
        ApiError apiError = new ApiError(NOT_FOUND);
        apiError.setMessage(ex.getMessage());
        log.error("{} ", apiError.getMessage(), ex);
        return buildResponseEntity(apiError);
    }

  /**
   * Handles InvalidParameterException, InvalidValueException
   *
   * @param ex the InvalidParameterException
   * @return the ApiError object
   */
  @ExceptionHandler({InvalidParameterException.class, InvalidValueException.class})
    protected ResponseEntity<Object> handleInvalidParameter(RuntimeException ex) {
        log.warn("handleInvalidParameter or InvalidValue", ex);
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        log.error("{} ", apiError.getMessage(), ex);
        return buildResponseEntity(apiError);
    }

  /**
   * Handles IllegalArgumentException
   *
   * @param ex the InvalidParameterException
   * @return the ApiError object
   */
  @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleInvalidParameter(IllegalArgumentException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        log.error("{} ",apiError.getMessage(), ex);
        return buildResponseEntity(apiError);
    }

    /**
     * Handles MethodArgumentNotValidException. Triggered when an object fails @Valid validation.
     *
     * @param ex      the MethodArgumentNotValidException that is thrown when @Valid validation fails
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        log.warn("handleMethodArgumentNotValid", ex);
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage("Validation error");
        apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());
        apiError.addValidationError(ex.getBindingResult().getGlobalErrors());
        log.error("{} ", apiError.getMessage(), ex);
        return buildResponseEntity(apiError);
    }


}
