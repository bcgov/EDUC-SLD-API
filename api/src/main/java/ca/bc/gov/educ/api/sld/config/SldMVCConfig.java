package ca.bc.gov.educ.api.sld.config;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The type sld mvc config.
 */
@Configuration
public class SldMVCConfig implements WebMvcConfigurer {

    @Getter(AccessLevel.PRIVATE)
    private final RequestResponseInterceptor requestResponseInterceptor;

  /**
   * Instantiates a new sld mvc config.
   *
   * @param requestResponseInterceptor the sld request interceptor
   */
  @Autowired
  public SldMVCConfig(final RequestResponseInterceptor requestResponseInterceptor) {
    this.requestResponseInterceptor = requestResponseInterceptor;
  }

  @Override
  public void addInterceptors(final InterceptorRegistry registry) {
    registry.addInterceptor(this.requestResponseInterceptor).addPathPatterns("/**");
  }
}
