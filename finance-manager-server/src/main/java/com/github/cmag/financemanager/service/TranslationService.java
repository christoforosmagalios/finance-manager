package com.github.cmag.financemanager.service;

import java.util.Locale;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TranslationService {

  @Value("${finance.manager.default.language}")
  private String defaultLanguage;

  private final MessageSource messageSource;

  /**
   * Translate the given key in the given language. If the language is null, translate it in the
   * default language.
   *
   * @param key The translation key.
   * @param language The language.
   * @return The translated value.
   */
  public String translate(String key, String language) {
    Locale locale = new Locale(Objects.isNull(language) ? defaultLanguage : language);
    return messageSource.getMessage(key, null, locale);
  }
}
