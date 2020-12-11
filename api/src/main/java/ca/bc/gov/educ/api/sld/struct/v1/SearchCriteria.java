package ca.bc.gov.educ.api.sld.struct.v1;

import ca.bc.gov.educ.api.sld.filter.FilterOperation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * The type Search criteria.
 *
 * @author OM
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SearchCriteria {
  /**
   * The Key.
   */
  @NotNull
  String key;
  /**
   * The Operation.
   */
  @NotNull
  FilterOperation operation;
  /**
   * The Value.
   */
  String value;
  /**
   * The Value type.
   */
  @NotNull
  ValueType valueType;

  /**
   * The Condition. ENUM to hold and AND OR
   */
  Condition condition;
}
