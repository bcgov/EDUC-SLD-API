package ca.bc.gov.educ.api.sld.service;

import ca.bc.gov.educ.api.sld.model.SldStudentEntity;
import ca.bc.gov.educ.api.sld.model.SldStudentId;
import ca.bc.gov.educ.api.sld.repository.SldRepository;
import ca.bc.gov.educ.api.sld.struct.v1.SldStudent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import java.util.List;

import static ca.bc.gov.educ.api.sld.jooq.Tables.STUDENT;

/**
 * The type sld student service.
 */
@Service
@Slf4j
public class SldStudentService extends SldBaseService {

  @Getter(AccessLevel.PRIVATE)
  private final SldRepository sldRepository;

  /**
   * Instantiates a new sld student service.
   *
   * @param emf the EntityManagerFactory
   * @param sldRepository the sld repository
   * @param create the DSLContext
   */
  @Autowired
  public SldStudentService(final EntityManagerFactory emf, final SldRepository sldRepository, final DSLContext create) {
    super(emf, create, STUDENT, STUDENT.PEN);
    this.sldRepository = sldRepository;
  }

  /**
   * Gets sld by pen.
   *
   * @param pen the pen
   * @return the sld by pen
   */
  public List<SldStudentEntity> getSldByPen(String pen) {
    return getSldRepository().findAll(Example.of(SldStudentEntity.builder().sldStudentId(SldStudentId.builder().pen(pen).build()).build()));
  }

  /**
   * Update sld students by pen. Only attributes without null will be updated.
   *
   * @param pen the PEN
   * @param sldStudent the Sld Student data
   * @return the SldStudentEntity list
   */
  public List<SldStudentEntity> updateSldStudentsByPen(String pen, SldStudent sldStudent) {
    int count = updateSldDataByPen(pen, sldStudent);
    if(count > 0) {
      return getSldRepository().findAll(Example.of(SldStudentEntity.builder().sldStudentId(SldStudentId.builder().pen(pen.equals(sldStudent.getPen()) ? pen : sldStudent.getPen()).build()).build()));
    } else {
      return List.of();
    }
  }

}
