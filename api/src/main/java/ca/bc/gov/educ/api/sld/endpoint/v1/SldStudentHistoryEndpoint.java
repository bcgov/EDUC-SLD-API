package ca.bc.gov.educ.api.sld.endpoint.v1;

import ca.bc.gov.educ.api.sld.struct.v1.SldStudentHistory;
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

@RequestMapping("/api/v1/student-history")
@OpenAPIDefinition(info = @Info(title = "API for SLD Student History data.", description = "This Read API is for Reading the SLD history data of a student in BC from open vms system.", version = "1"), security = {@SecurityRequirement(name = "OAUTH2", scopes = {"READ_SLD_STUDENT_HISTORY"})})

public interface SldStudentHistoryEndpoint {
  /**
   * Gets sld student history data by pen.
   *
   * @param pen the pen
   * @return the sld student history data by pen
   */
  @GetMapping("/")
  @PreAuthorize("hasAuthority('SCOPE_READ_SLD_STUDENT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  List<SldStudentHistory> getSldStudentHistoryByPen(@RequestParam() String pen);
}
