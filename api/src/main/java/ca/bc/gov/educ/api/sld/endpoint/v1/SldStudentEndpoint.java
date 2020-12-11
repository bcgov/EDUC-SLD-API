package ca.bc.gov.educ.api.sld.endpoint.v1;

import ca.bc.gov.educ.api.sld.struct.v1.SldStudent;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * The interface SLD Student endpoint.
 */
@RequestMapping("/api/v1/student")
@OpenAPIDefinition(info = @Info(title = "API for SLD Student data.", description = "This Read API is for Reading the SLD data of a student in BC from open vms system.", version = "1"), security = {@SecurityRequirement(name = "OAUTH2", scopes = {"READ_SLD_STUDENT"})})
public interface SldStudentEndpoint {

  /**
   * Gets sld student data by pen.
   *
   * @param pen the pen
   * @return the sld student data by pen
   */
  @GetMapping("/")
  @PreAuthorize("#oauth2.hasScope('READ_SLD_STUDENT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  List<SldStudent> getSldStudentByPen(@RequestParam() String pen);

}
