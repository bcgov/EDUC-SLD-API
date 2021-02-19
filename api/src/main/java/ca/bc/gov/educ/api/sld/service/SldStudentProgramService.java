package ca.bc.gov.educ.api.sld.service;

import ca.bc.gov.educ.api.sld.model.SldStudentProgramEntity;
import ca.bc.gov.educ.api.sld.repository.SldStudentProgramRepository;
import ca.bc.gov.educ.api.sld.struct.v1.SldStudentProgram;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import java.util.List;

import static ca.bc.gov.educ.api.sld.jooq.Tables.STUDENT_PROGRAMS;

/**
 * The type sld student program service.
 */
@Service
@Slf4j
public class SldStudentProgramService extends SldBaseService {

  @Getter(AccessLevel.PRIVATE)
  private final SldStudentProgramRepository sldStudentProgramRepository;

  /**
   * Instantiates a new sld student program service.
   *
   * @param emf the EntityManagerFactory
   * @param sldStudentProgramRepository the sld student program repo
   * @param create the DSLContext
   */
  @Autowired
  public SldStudentProgramService(final EntityManagerFactory emf, final SldStudentProgramRepository sldStudentProgramRepository, final DSLContext create) {
    super(emf, create, STUDENT_PROGRAMS, STUDENT_PROGRAMS.PEN);
    this.sldStudentProgramRepository = sldStudentProgramRepository;
  }

  /**
   * Gets Student Programs by pen.
   *
   * @param pen the pen
   * @return the Student Program list
   */
  public List<SldStudentProgramEntity> getStudentProgramsByPen(String pen) {
    return getSldStudentProgramRepository().findAllByPen(pen);
  }

  /**
   * Update Student Programs by pen.
   *
   * @param pen the PEN
   * @param sldStudentProgram the Sld Student Program data
   * @return the SldStudentProgramEntity list
   */
  public List<SldStudentProgramEntity> updateStudentProgramsByPen(String pen, SldStudentProgram sldStudentProgram) {
    int count = updateSldDataByPen(pen, sldStudentProgram);
    if(count > 0) {
      return getSldStudentProgramRepository().findAllByPen(pen.equals(sldStudentProgram.getPen()) ? pen : sldStudentProgram.getPen());
    } else {
      return List.of();
    }
  }


}
