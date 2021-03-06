package ca.bc.gov.educ.api.sld.controller.v1;

import ca.bc.gov.educ.api.sld.endpoint.v1.SldStudentEndpoint;
import ca.bc.gov.educ.api.sld.mappers.v1.SldStudentMapper;
import ca.bc.gov.educ.api.sld.service.SldStudentService;
import ca.bc.gov.educ.api.sld.struct.v1.SldStudent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The type sld student controller.
 */
@RestController
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
  public List<SldStudent> getSldStudentsByPen(String pen) {
    return getSldStudentService().getSldByPen(pen).stream().filter(Objects::nonNull).map(mapper::toStructure).collect(Collectors.toList());
  }
}
