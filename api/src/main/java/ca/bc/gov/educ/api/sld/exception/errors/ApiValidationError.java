package ca.bc.gov.educ.api.sld.exception.errors;

/**
 * The type Api validation error.
 */
class ApiValidationError implements ApiSubError {
	private String object;
	private String field;
	private Object rejectedValue;
	private String message;

  /**
   * Instantiates a new Api validation error.
   *
   * @param object  the object
   * @param message the message
   */
  ApiValidationError(String object, String message) {
		this.object = object;
		this.message = message;
	}

  /**
   * Instantiates a new Api validation error.
   *
   * @param object        the object
   * @param field         the field
   * @param rejectedValue the rejected value
   * @param message       the message
   */
  public ApiValidationError(String object, String field, Object rejectedValue, String message) {
		super();
		this.object = object;
		this.field = field;
		this.rejectedValue = rejectedValue;
		this.message = message;
	}

  /**
   * Gets object.
   *
   * @return the object
   */
  public String getObject() {
		return object;
	}

  /**
   * Sets object.
   *
   * @param object the object
   */
  public void setObject(String object) {
		this.object = object;
	}

  /**
   * Gets field.
   *
   * @return the field
   */
  public String getField() {
		return field;
	}

  /**
   * Sets field.
   *
   * @param field the field
   */
  public void setField(String field) {
		this.field = field;
	}

  /**
   * Gets rejected value.
   *
   * @return the rejected value
   */
  public Object getRejectedValue() {
		return rejectedValue;
	}

  /**
   * Sets rejected value.
   *
   * @param rejectedValue the rejected value
   */
  public void setRejectedValue(Object rejectedValue) {
		this.rejectedValue = rejectedValue;
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


}
