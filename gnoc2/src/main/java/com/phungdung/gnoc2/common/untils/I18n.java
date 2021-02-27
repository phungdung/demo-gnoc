package com.phungdung.gnoc2.common.untils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

@Slf4j
public class I18n {

  public static String getString(String code, Object... objects) {
    try {
      Locale locale = LocaleContextHolder.getLocale();
      MessageSource messageSource = SpringApplicationContext.bean(MessageSource.class);
      return messageSource.getMessage(code, objects, locale);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return code;
    }
  }

  public static String getLanguage(String code, Object... objects) {
    try {
      Locale locale = LocaleContextHolder.getLocale();
      MessageSource messageSource = SpringApplicationContext.bean(MessageSource.class);
      return messageSource.getMessage("language." + code, objects, locale);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return code;
    }
  }

  public static String getMessages(String code, Object... objects) {
    try {
      Locale locale = LocaleContextHolder.getLocale();
      MessageSource messageSource = SpringApplicationContext.bean(MessageSource.class);
      return messageSource.getMessage("messages." + code, objects, locale);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return code;
    }
  }

  public static String getValidation(String code, Object... objects) {
    try {
      Locale locale = LocaleContextHolder.getLocale();
      MessageSource messageSource = SpringApplicationContext.bean(MessageSource.class);
      return messageSource.getMessage("validation." + code, objects, locale);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return code;
    }
  }

  public static String getLanguageByLocale(Locale locale, String code, Object... objects) {
    try {
      MessageSource messageSource = SpringApplicationContext.bean(MessageSource.class);
      return messageSource.getMessage("language." + code, objects, locale);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return code;
    }
  }

  public static String getLocale() {
    return LocaleContextHolder.getLocale().toString();
  }

  public static void setLocale(String strLocale) {
    Locale locale;
    if (strLocale != null && strLocale != "") {
      if (strLocale.equalsIgnoreCase("en") || strLocale.equalsIgnoreCase("en_us")) {
        locale = new Locale("en", "US");
      } else if (strLocale.equalsIgnoreCase("vi") || strLocale.equalsIgnoreCase("vi_vn")) {
        locale = new Locale("vi", "VN");
      } else {
        locale = new Locale("vi", "VN");
      }
    } else {
      locale = new Locale("vi", "VN");
    }
    LocaleContextHolder.setLocale(locale);
  }
}
