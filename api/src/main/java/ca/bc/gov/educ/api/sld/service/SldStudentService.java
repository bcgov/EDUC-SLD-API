package ca.bc.gov.educ.api.sld.service;

import ca.bc.gov.educ.api.sld.model.SldStudentEntity;
import ca.bc.gov.educ.api.sld.repository.SldRepository;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type sld student service.
 */
@Service
public class SldStudentService {

  @Getter(AccessLevel.PRIVATE)
  private final SldRepository sldRepository;

  /**
   * Instantiates a new sld student service.
   *
   * @param sldRepository the sld repository
   */
  @Autowired
  public SldStudentService(final SldRepository sldRepository) {
    this.sldRepository = sldRepository;
  }

  /**
   * Gets sld by pen.
   *
   * @param pen the pen
   * @return the sld by pen
   */
  public List<SldStudentEntity> getSldByPen(String pen) {
    System.out.println(getSldRepository().findAllByPen(pen));
    return getSldRepository().findAllByPen(pen);
  }
}
