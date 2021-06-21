package ca.bc.gov.educ.api.sld.repository;

import ca.bc.gov.educ.api.sld.model.SldDiaStudentEntity;
import ca.bc.gov.educ.api.sld.model.SldDiaStudentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * The interface SLD DIA Student repository.
 */
@Repository
public interface SldDiaStudentRepository extends JpaRepository<SldDiaStudentEntity, SldDiaStudentId>, JpaSpecificationExecutor<SldDiaStudentEntity> {
}
