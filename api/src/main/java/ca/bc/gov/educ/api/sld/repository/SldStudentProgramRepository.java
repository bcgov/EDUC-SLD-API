package ca.bc.gov.educ.api.sld.repository;

import ca.bc.gov.educ.api.sld.model.SldStudentProgramEntity;
import ca.bc.gov.educ.api.sld.model.SldStudentProgramId;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface SLD Student Program repository.
 */
@Repository
public interface SldStudentProgramRepository extends CrudRepository<SldStudentProgramEntity, SldStudentProgramId>, JpaSpecificationExecutor<SldStudentProgramEntity> {
  /**
   * Find by pen optional.
   *
   * @param pen the pen
   * @return the SldStudentProgramEntity list
   */
  List<SldStudentProgramEntity> findAllByPen(String pen);
}
