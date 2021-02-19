package ca.bc.gov.educ.api.sld.repository;

import ca.bc.gov.educ.api.sld.model.SldDiaStudentEntity;
import ca.bc.gov.educ.api.sld.model.SldDiaStudentId;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface SLD DIA Student repository.
 */
@Repository
public interface SldDiaStudentRepository extends CrudRepository<SldDiaStudentEntity, SldDiaStudentId>, JpaSpecificationExecutor<SldDiaStudentEntity> {
  /**
   * Find by pen optional.
   *
   * @param pen the pen
   * @return the SldDiaStudentEntity list
   */
  List<SldDiaStudentEntity> findAllBySldDiaStudentIdPen(String pen);
}
