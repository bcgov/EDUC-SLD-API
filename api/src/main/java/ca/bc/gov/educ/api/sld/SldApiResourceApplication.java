package ca.bc.gov.educ.api.sld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * The type Sld api resource application.
 */
@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableCaching
public class SldApiResourceApplication {

  /**
   * The entry point of application.
   *
   * @param args the input arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(SldApiResourceApplication.class, args);
  }

  /**
   * The type Web security configuration.
   * Add security exceptions for swagger UI and prometheus.
   */
  @Configuration
  static
  class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * Instantiates a new Web security configuration.
     * This makes sure that security context is propagated to async threads as well.
     */
    public WebSecurityConfiguration() {
      super();
      SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    /**
     * Configure paths to be excluded from security.
     *
     * @param web the web
     */
    @Override
    public void configure(final WebSecurity web) {
      web.ignoring().antMatchers("/v3/api-docs/**",
        "/actuator/health", "/actuator/prometheus", "/actuator/metrics/**",
        "/swagger-ui/**");
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
      http
        .authorizeRequests()
        .anyRequest().authenticated().and()
        .oauth2ResourceServer().jwt();
    }
  }
}
