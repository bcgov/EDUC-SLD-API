package ca.bc.gov.educ.api.sld.filter;

import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.function.Function;

/**
 * Filter Criteria Holder
 *
 * @param <T> is the java type of the DB table column
 * @author om
 */
public class FilterCriteria<T extends Comparable<T>> {

  /**
   * Holds the operation {@link FilterOperation}
   */
  private final FilterOperation operation;

  /**
   * Table column name
   */
  private final String fieldName;

  /**
   * Holds the Function to convertString to <T>
   */
  private final Function<String, T> converterFunction;

  /**
   * Converted value
   */
  private T convertedSingleValue;



  /**
   * Instantiates a new Filter criteria.
   *
   * @param fieldName         the field name
   * @param fieldValue        the field value
   * @param filterOperation   the filter operation
   * @param converterFunction the converter function
   */
  public FilterCriteria(@NotNull String fieldName, @NotNull String fieldValue, @NotNull FilterOperation filterOperation, Function<String, T> converterFunction) {

    this.fieldName = fieldName;
    this.converterFunction = converterFunction;

    // Split the fieldValue value as comma separated.
    String[] operationValues = StringUtils.split(fieldValue, ",");
    this.operation = filterOperation;
    // Validate other conditions
    validateAndAssign(operationValues);

  }

  private void validateAndAssign(String[] operationValues) {

    this.convertedSingleValue = converterFunction.apply(operationValues[0]);
  }

  /**
   * Gets converted single value.
   *
   * @return the converted single value
   */
  public T getConvertedSingleValue() {
    return convertedSingleValue;
  }


  /**
   * Gets operation.
   *
   * @return the operation
   */
  public FilterOperation getOperation() {
    return operation;
  }

  /**
   * Gets field name.
   *
   * @return the field name
   */
  public String getFieldName() {
    return fieldName;
  }


}
