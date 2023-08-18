package ca.bc.gov.educ.api.sld.filter;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.function.Function;

/**
 * The type Filter specifications.
 *
 * @param <E> the type parameter
 * @param <T> the type parameter
 */
@Service
public class FilterSpecifications<E, T extends Comparable<T>> {

	private EnumMap<FilterOperation, Function<FilterCriteria<T>, Specification<E>>> map;

  /**
   * Instantiates a new Filter specifications.
   */
  public FilterSpecifications() {
		initSpecifications();
	}

  /**
   * Gets specification.
   *
   * @param operation the operation
   * @return the specification
   */
  public Function<FilterCriteria<T>, Specification<E>> getSpecification(FilterOperation operation) {
		return map.get(operation);
	}

  /**
   * Init specifications.
   */
  @PostConstruct
	public void initSpecifications() {

		map = new EnumMap<>(FilterOperation.class);

		// Equal
		map.put(FilterOperation.EQUAL, filterCriteria -> (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder
				.equal(root.get(filterCriteria.getFieldName()), filterCriteria.getConvertedSingleValue()));

	}
}
