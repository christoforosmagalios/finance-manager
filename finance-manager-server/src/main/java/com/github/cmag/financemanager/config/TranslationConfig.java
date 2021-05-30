package com.github.cmag.financemanager.config;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * Translation Configuration class.
 */
@Configuration
public class TranslationConfig {

  @Value("${finance.manager.default.language}")
  private String defaultLanguage;

  @Bean
  public ResourceBundleMessageSource messageSource() {

    var source = new ResourceBundleMessageSource();
    source.setBasenames("messages/translation");
    source.setUseCodeAsDefaultMessage(false);
    source.setDefaultEncoding("UTF-8");
    source.setDefaultLocale(new Locale(defaultLanguage));

    return source;
  }
}
