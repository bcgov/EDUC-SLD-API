package ca.bc.gov.educ.api.sld.repository;

import ca.bc.gov.educ.api.sld.model.SldStudentEntity;
import ca.bc.gov.educ.api.sld.model.SldStudentId;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface sld repository.
 */
@Repository
public interface SldRepository extends CrudRepository<SldStudentEntity, SldStudentId>, JpaSpecificationExecutor<SldStudentEntity> {
  /**
   * Find by pen optional.
   *
   * @param pen the pen
   * @return the optional
   */
  List<SldStudentEntity> findAllByPen(String pen);
}
