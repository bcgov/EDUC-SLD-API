package ca.bc.gov.educ.api.sld.service;

import ca.bc.gov.educ.api.sld.model.SldDiaStudentEntity;
import ca.bc.gov.educ.api.sld.repository.SldDiaStudentRepository;
import ca.bc.gov.educ.api.sld.struct.v1.SldDiaStudent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import java.util.List;

import static ca.bc.gov.educ.api.sld.jooq.Tables.DIA_STUDENT;

/**
 * The type sld dia student service.
 */
@Service
@Slf4j
public class SldDiaStudentService extends SldBaseService {

  @Getter(AccessLevel.PRIVATE)
  private final SldDiaStudentRepository sldDiaStudentRepository;

  /**
   * Instantiates a new sld student service.
   *
   * @param emf the EntityManagerFactory
   * @param sldDiaStudentRepository the sld dia student repo
   * @param create the DSLContext
   */
  @Autowired
  public SldDiaStudentService(final EntityManagerFactory emf, final SldDiaStudentRepository sldDiaStudentRepository, final DSLContext create) {
    super(emf, create, DIA_STUDENT, DIA_STUDENT.PEN);
    this.sldDiaStudentRepository = sldDiaStudentRepository;
  }

  /**
   * Gets DiaStudents by pen.
   *
   * @param pen the pen
   * @return the DiaStudent list
   */
  public List<SldDiaStudentEntity> getDiaStudentByPen(String pen) {
    return getSldDiaStudentRepository().findAllBySldDiaStudentIdPen(pen);
  }

  /**
   * Update DiaStudents by pen.
   *
   * @param pen the PEN
   * @param sldDiaStudent the Sld DIA Student data
   * @return the SldDiaStudentEntity list
   */
  public List<SldDiaStudentEntity> updateDiaStudentsByPen(String pen, SldDiaStudent sldDiaStudent) {
    int count = updateSldDataByPen(pen, sldDiaStudent);
    if(count > 0) {
      return getSldDiaStudentRepository().findAllBySldDiaStudentIdPen(pen.equals(sldDiaStudent.getPen()) ? pen : sldDiaStudent.getPen());
    } else {
      return List.of();
    }
  }

}
