package ca.bc.gov.educ.api.sld.controller.v1;

import ca.bc.gov.educ.api.sld.SldApiResourceApplication;
import ca.bc.gov.educ.api.sld.mappers.v1.SldStudentMapper;
import ca.bc.gov.educ.api.sld.repository.SldRepository;
import ca.bc.gov.educ.api.sld.support.SldTestUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = {SldApiResourceApplication.class})
@AutoConfigureMockMvc
@SuppressWarnings("squid:S00100")
public class SldStudentControllerTest {
  @Autowired
  private MockMvc mvc;
  private static final SldStudentMapper mapper = SldStudentMapper.mapper;
  @Autowired
  SldRepository repository;
  @Autowired
  SldStudentController controller;

  @Before
  public void setUp() throws IOException {
    MockitoAnnotations.openMocks(this);
    SldTestUtil.createSampleDBData("SldSampleStudentData.json", new TypeReference<>() {}, repository, mapper::toModel);
  }

  @After
  public void tearDown() {
    repository.deleteAll();
  }

  @Test
  public void testGetSldStudentByPen_GivenPenExistInDB_ShouldReturnStatusOk() throws Exception {

    System.out.println(repository.findAllByPen("120164447"));
    this.mvc.perform(get("/api/v1/student/")
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "READ_SLD_STUDENT")))
        .param("pen", "120164447"))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.length()", is(3)))
        .andExpect(jsonPath("$[0].pen", is("120164447")))
        .andExpect(jsonPath("$[0].legalSurname", is("Larusso".toUpperCase())))
        .andExpect(jsonPath("$[0].legalGivenName", is("Daniel".toUpperCase())))
        .andExpect(jsonPath("$[0].birthDate", is("19980410")));

  }

  @Test
  public void testGetSldStudentByPen_GivenPenDoesNotExistInDB_ShouldReturnEmptyArray() throws Exception {

    this.mvc.perform(get("/api/v1/student/")
            .with(jwt().jwt((jwt) -> jwt.claim("scope", "READ_SLD_STUDENT")))
            .param("pen", "7613009911")
        ).andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()", is(0)));


  }
}
