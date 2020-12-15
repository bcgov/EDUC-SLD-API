package ca.bc.gov.educ.api.sld.controller.v1;

import ca.bc.gov.educ.api.sld.endpoint.v1.SldStudentHistoryEndpoint;
import ca.bc.gov.educ.api.sld.mappers.v1.SldStudentHistoryMapper;
import ca.bc.gov.educ.api.sld.model.SldStudentHistoryEntity;
import ca.bc.gov.educ.api.sld.service.SldStudentHistoryService;
import ca.bc.gov.educ.api.sld.struct.v1.SldStudentHistory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The type sld student history controller.
 */
@RestController
@EnableResourceServer
@Slf4j
public class SldStudentHistoryController implements SldStudentHistoryEndpoint {

  @Getter(AccessLevel.PRIVATE)
  private final SldStudentHistoryService sldStudentHistoryService;
  private static final SldStudentHistoryMapper mapper = SldStudentHistoryMapper.mapper;

  /**
   * Instantiates a new sld student history controller.
   *
   * @param sldStudentHistoryService the sld student history service
   */
  @Autowired
  public SldStudentHistoryController(final SldStudentHistoryService sldStudentHistoryService) { this.sldStudentHistoryService = sldStudentHistoryService; }

  @Override
  public List<SldStudentHistory> getSldStudentHistoryByPen(String pen) {
    log.debug("Retrieving Student History Data by PEN");
    List<SldStudentHistoryEntity> sldStudentHistoryResponse = getSldStudentHistoryService().getSldByPen(pen);
    return sldStudentHistoryResponse.stream().map(mapper::toStructure).collect(Collectors.toList());
  }
}
