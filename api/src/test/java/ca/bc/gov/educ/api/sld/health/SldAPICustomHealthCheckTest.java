package ca.bc.gov.educ.api.sld.health;

import ca.bc.gov.educ.api.sld.controller.v1.BaseSLDAPITest;
import io.nats.client.Connection;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Status;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class SldAPICustomHealthCheckTest extends BaseSLDAPITest {

  @Autowired
  Connection natsConnection;

  @Autowired
  private SldAPICustomHealthCheck sldAPICustomHealthCheck;

  @Test
  public void testGetHealth_givenClosedNatsConnection_shouldReturnStatusDown() {
    when(natsConnection.getStatus()).thenReturn(Connection.Status.CLOSED);
    assertThat(sldAPICustomHealthCheck.getHealth(true)).isNotNull();
    assertThat(sldAPICustomHealthCheck.getHealth(true).getStatus()).isEqualTo(Status.DOWN);
  }

  @Test
  public void testGetHealth_givenOpenNatsConnection_shouldReturnStatusUp() {
    when(natsConnection.getStatus()).thenReturn(Connection.Status.CONNECTED);
    assertThat(sldAPICustomHealthCheck.getHealth(true)).isNotNull();
    assertThat(sldAPICustomHealthCheck.getHealth(true).getStatus()).isEqualTo(Status.UP);
  }


  @Test
  public void testHealth_givenClosedNatsConnection_shouldReturnStatusDown() {
    when(natsConnection.getStatus()).thenReturn(Connection.Status.CLOSED);
    assertThat(sldAPICustomHealthCheck.health()).isNotNull();
    assertThat(sldAPICustomHealthCheck.health().getStatus()).isEqualTo(Status.DOWN);
  }

  @Test
  public void testHealth_givenOpenNatsConnection_shouldReturnStatusUp() {
    when(natsConnection.getStatus()).thenReturn(Connection.Status.CONNECTED);
    assertThat(sldAPICustomHealthCheck.health()).isNotNull();
    assertThat(sldAPICustomHealthCheck.health().getStatus()).isEqualTo(Status.UP);
  }
}
