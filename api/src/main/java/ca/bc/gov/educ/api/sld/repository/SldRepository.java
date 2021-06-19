package ca.bc.gov.educ.api.sld.repository;

import ca.bc.gov.educ.api.sld.model.SldStudentEntity;
import ca.bc.gov.educ.api.sld.model.SldStudentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * The interface sld repository.
 */
@Repository
public interface SldRepository extends JpaRepository<SldStudentEntity, SldStudentId>, JpaSpecificationExecutor<SldStudentEntity> {
}
