package com.github.cmag.financemanager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger configuration.
 */
@Configuration
public class SwaggerConfig {

  @Value("${swagger.enabled}")
  private boolean isSwaggerEnabled;

  /**
   * Define a the Docket Bean.
   *
   * @return The new Docket.
   */
  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.any())
        .build()
        .enable(isSwaggerEnabled);
  }
}
