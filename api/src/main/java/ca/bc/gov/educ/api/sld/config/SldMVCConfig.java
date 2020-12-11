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
    private final SldRequestInterceptor sldRequestInterceptor;

  /**
   * Instantiates a new sld mvc config.
   *
   * @param sldRequestInterceptor the sld request interceptor
   */
  @Autowired
    public SldMVCConfig(final SldRequestInterceptor sldRequestInterceptor){
        this.sldRequestInterceptor = sldRequestInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sldRequestInterceptor).addPathPatterns("/**/**/");
    }
}
