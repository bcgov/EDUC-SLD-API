package ca.bc.gov.educ.api.sld.service;

import ca.bc.gov.educ.api.sld.model.SldDiaStudentEntity;
import ca.bc.gov.educ.api.sld.repository.SldDiaStudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class SldDiaStudentService {
  private final SldDiaStudentRepository sldDiaStudentRepository;

  public SldDiaStudentService(final SldDiaStudentRepository sldDiaStudentRepository) {
    this.sldDiaStudentRepository = sldDiaStudentRepository;
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public List<SldDiaStudentEntity> createDiaStudents(final List<SldDiaStudentEntity> sldDiaStudentEntities) {
    return this.sldDiaStudentRepository.saveAll(sldDiaStudentEntities);
  }

}
