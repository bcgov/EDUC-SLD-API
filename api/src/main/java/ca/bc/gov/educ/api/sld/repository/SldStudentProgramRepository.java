package ca.bc.gov.educ.api.sld.repository;

import ca.bc.gov.educ.api.sld.model.SldStudentProgramEntity;
import ca.bc.gov.educ.api.sld.model.SldStudentProgramId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * The interface SLD Student Program repository.
 */
@Repository
public interface SldStudentProgramRepository extends JpaRepository<SldStudentProgramEntity, SldStudentProgramId>, JpaSpecificationExecutor<SldStudentProgramEntity> {
}
