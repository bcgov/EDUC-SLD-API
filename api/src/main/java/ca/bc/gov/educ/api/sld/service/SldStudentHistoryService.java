package ca.bc.gov.educ.api.sld.service;

import ca.bc.gov.educ.api.sld.model.SldStudentHistoryEntity;
import ca.bc.gov.educ.api.sld.repository.SldStudentHistoryRepository;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type sld student history service.
 */
@Service
public class SldStudentHistoryService {

  @Getter(AccessLevel.PRIVATE)
  private final SldStudentHistoryRepository sldStudentHistoryRepository;

  /**
   * Instantiates a new sld student service.
   *
   * @param sldStudentHistoryRepository the sld repository
   */
  @Autowired
  public SldStudentHistoryService(final SldStudentHistoryRepository sldStudentHistoryRepository) {
    this.sldStudentHistoryRepository = sldStudentHistoryRepository;
  }

  /**
   * Gets sld by pen.
   *
   * @param pen the pen
   * @return the sld by pen
   */
  public List<SldStudentHistoryEntity> getSldByPen(String pen) {
    return getSldStudentHistoryRepository().findAllByPen(pen);
  }
}
