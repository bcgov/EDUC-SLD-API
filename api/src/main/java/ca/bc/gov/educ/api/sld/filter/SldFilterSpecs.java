package ca.bc.gov.educ.api.sld.filter;

import ca.bc.gov.educ.api.sld.model.SldStudentEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.function.Function;

/**
 * The type sld filter specs.
 */
@Service
@Slf4j
public class SldFilterSpecs {

  private final FilterSpecifications<SldStudentEntity, String> stringFilterSpecifications;
  private final Converters converters;

  /**
   * Instantiates a new sld filter specs.
   *
   * @param stringFilterSpecifications   the string filter specifications
   * @param converters                   the converters
   */
  public SldFilterSpecs(FilterSpecifications<SldStudentEntity, String> stringFilterSpecifications, Converters converters) {
    this.stringFilterSpecifications = stringFilterSpecifications;
    this.converters = converters;
  }


  /**
   * Gets string type specification.
   *
   * @param fieldName       the field name
   * @param filterValue     the filter value
   * @param filterOperation the filter operation
   * @return the string type specification
   */
  public Specification<SldStudentEntity> getStringTypeSpecification(String fieldName, String filterValue, FilterOperation filterOperation) {
    return getSpecification(fieldName, filterValue, filterOperation, converters.getFunction(String.class), stringFilterSpecifications);
  }


  private <T extends Comparable<T>> Specification<SldStudentEntity> getSpecification(String fieldName,
                                                                                     String filterValue,
                                                                                     FilterOperation filterOperation,
                                                                                     Function<String, T> converter,
                                                                                     FilterSpecifications<SldStudentEntity, T> specifications) {
    FilterCriteria<T> criteria = new FilterCriteria<>(fieldName, filterValue, filterOperation, converter);
    return specifications.getSpecification(criteria.getOperation()).apply(criteria);
  }
}
