package ca.bc.gov.educ.api.sld.repository;

import ca.bc.gov.educ.api.sld.model.SldStudentHistoryEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface sld student history repository.
 */
@Repository
public interface SldStudentHistoryRepository extends CrudRepository<SldStudentHistoryEntity, String>, JpaSpecificationExecutor<SldStudentHistoryEntity> {

  /**
   * Find by pen optional.
   *
   * @param pen the pen
   * @return the optional
   */
  List<SldStudentHistoryEntity> findAllByPen(String pen);
}
