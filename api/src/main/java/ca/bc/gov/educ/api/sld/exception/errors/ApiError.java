package ca.bc.gov.educ.api.sld.exception.errors;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Api error.
 */
public class ApiError {

	private HttpStatus status;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	private LocalDateTime timestamp;
	private String message;
	private String debugMessage;
	private List<ApiSubError> subErrors;

	private ApiError() {
		timestamp = LocalDateTime.now();
	}

  /**
   * Instantiates a new Api error.
   *
   * @param status the status
   */
  public ApiError(HttpStatus status) {
		this();
		this.status = status;
	}

  /**
   * Instantiates a new Api error.
   *
   * @param status the status
   * @param ex     the ex
   */
  ApiError(HttpStatus status, Throwable ex) {
		this();
		this.status = status;
		this.message = "Unexpected error";
		this.debugMessage = ex.getLocalizedMessage();
	}

  /**
   * Instantiates a new Api error.
   *
   * @param status  the status
   * @param message the message
   * @param ex      the ex
   */
  public ApiError(HttpStatus status, String message, Throwable ex) {
		this();
		this.status = status;
		this.message = message;
		this.debugMessage = ex.getLocalizedMessage();
	}

	private void addSubError(ApiSubError subError) {
		if (subErrors == null) {
			subErrors = new ArrayList<>();
		}
		subErrors.add(subError);
	}

	private void addValidationError(String object, String field, Object rejectedValue, String message) {
		addSubError(new ApiValidationError(object, field, rejectedValue, message));
	}

	private void addValidationError(String object, String message) {
		addSubError(new ApiValidationError(object, message));
	}

	private void addValidationError(FieldError fieldError) {
		this.addValidationError(fieldError.getObjectName(), fieldError.getField(), fieldError.getRejectedValue(),
				fieldError.getDefaultMessage());
	}

  /**
   * Add validation errors.
   *
   * @param fieldErrors the field errors
   */
  public void addValidationErrors(List<FieldError> fieldErrors) {
		fieldErrors.forEach(this::addValidationError);
	}

	private void addValidationError(ObjectError objectError) {
		this.addValidationError(objectError.getObjectName(), objectError.getDefaultMessage());
	}

  /**
   * Add validation error.
   *
   * @param globalErrors the global errors
   */
  public void addValidationError(List<ObjectError> globalErrors) {
		globalErrors.forEach(this::addValidationError);
	}


  /**
   * Gets status.
   *
   * @return the status
   */
  public HttpStatus getStatus() {
		return status;
	}

  /**
   * Sets status.
   *
   * @param status the status
   */
  public void setStatus(HttpStatus status) {
		this.status = status;
	}

  /**
   * Gets timestamp.
   *
   * @return the timestamp
   */
  public LocalDateTime getTimestamp() {
		return timestamp;
	}

  /**
   * Sets timestamp.
   *
   * @param timestamp the timestamp
   */
  public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

  /**
   * Gets message.
   *
   * @return the message
   */
  public String getMessage() {
		return message;
	}

  /**
   * Sets message.
   *
   * @param message the message
   */
  public void setMessage(String message) {
		this.message = message;
	}

  /**
   * Gets debug message.
   *
   * @return the debug message
   */
  public String getDebugMessage() {
		return debugMessage;
	}

  /**
   * Sets debug message.
   *
   * @param debugMessage the debug message
   */
  public void setDebugMessage(String debugMessage) {
		this.debugMessage = debugMessage;
	}

  /**
   * Gets sub errors.
   *
   * @return the sub errors
   */
  public List<ApiSubError> getSubErrors() {
		return subErrors;
	}

  /**
   * Sets sub errors.
   *
   * @param subErrors the sub errors
   */
  public void setSubErrors(List<ApiSubError> subErrors) {
		this.subErrors = subErrors;
	}

}
