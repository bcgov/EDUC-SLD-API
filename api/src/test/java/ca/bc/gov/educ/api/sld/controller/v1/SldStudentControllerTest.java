package ca.bc.gov.educ.api.sld.controller.v1;

import ca.bc.gov.educ.api.sld.SldApiResourceApplication;
import ca.bc.gov.educ.api.sld.exception.RestExceptionHandler;
import ca.bc.gov.educ.api.sld.mappers.v1.SldStudentMapper;
import ca.bc.gov.educ.api.sld.repository.SldRepository;
import ca.bc.gov.educ.api.sld.struct.v1.SldStudent;
import ca.bc.gov.educ.api.sld.support.WithMockOAuth2Scope;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SldApiResourceApplication.class})
@SuppressWarnings("squid:S00100")
public class SldStudentControllerTest {
  private MockMvc mvc;
  private static final SldStudentMapper mapper = SldStudentMapper.mapper;
  @Autowired
  SldRepository repository;
  @Autowired
  SldStudentController controller;

  @Before
  public void setUp() throws IOException {
    MockitoAnnotations.openMocks(this);
    mvc = MockMvcBuilders.standaloneSetup(controller)
        .setControllerAdvice(new RestExceptionHandler()).build();
    final File file = new File(
        Objects.requireNonNull(getClass().getClassLoader().getResource("SldSampleStudentData.json")).getFile()
    );
    List<SldStudent> entities = new ObjectMapper().readValue(file, new TypeReference<>() {
    });
    repository.saveAll(entities.stream().map(mapper::toModel).collect(Collectors.toList()));
  }

  @After
  public void tearDown() {
    repository.deleteAll();
  }

  @Test
  @WithMockOAuth2Scope(scope = "READ_SLD_STUDENT")
  public void testGetSldStudentByPen_GivenPenExistInDB_ShouldReturnStatusOk() throws Exception {

    System.out.println(repository.findAllByPen("120164447"));
    this.mvc.perform(get("/api/v1/student/")
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
  @WithMockOAuth2Scope(scope = "READ_SLD_STUDENT")
  public void testGetSldStudentByPen_GivenPenDoesNotExistInDB_ShouldReturnEmptyArray() throws Exception {

    this.mvc.perform(get("/api/v1/student/").param("pen", "7613009911")
        ).andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()", is(0)));


  }
}
