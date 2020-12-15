package ca.bc.gov.educ.api.sld.controller.v1;

import ca.bc.gov.educ.api.sld.endpoint.v1.SldStudentEndpoint;
import ca.bc.gov.educ.api.sld.mappers.v1.SldStudentMapper;
import ca.bc.gov.educ.api.sld.model.SldStudentEntity;
import ca.bc.gov.educ.api.sld.service.SldStudentService;
import ca.bc.gov.educ.api.sld.struct.v1.SldStudent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The type sld student controller.
 */
@RestController
@EnableResourceServer
@Slf4j
public class SldStudentController implements SldStudentEndpoint {
  @Getter(AccessLevel.PRIVATE)
  private final SldStudentService sldStudentService;
  private static final SldStudentMapper mapper = SldStudentMapper.mapper;

  /**
   * Instantiates a new sld student controller.
   *
   * @param sldStudentService the sld student service
   */
  @Autowired
  public SldStudentController(final SldStudentService sldStudentService) {
    this.sldStudentService = sldStudentService;
  }

  @Override
  public List<SldStudent> getSldStudentByPen(String pen) {
    log.debug("Retrieving Student Data by PEN");
    List<SldStudentEntity> sldStudentsResponse = getSldStudentService().getSldByPen(pen);
    return sldStudentsResponse.stream().map(mapper::toStructure).collect(Collectors.toList());
  }

}
