package ca.bc.gov.educ.api.sld.controller.v1;

import ca.bc.gov.educ.api.sld.SldApiResourceApplication;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = {SldApiResourceApplication.class})
@AutoConfigureMockMvc
@SuppressWarnings("squid:S00100")
public abstract class BaseSLDAPITest {
  @Autowired
  protected MockMvc mvc;
}
