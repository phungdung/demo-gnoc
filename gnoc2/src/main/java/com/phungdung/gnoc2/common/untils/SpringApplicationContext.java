package com.phungdung.gnoc2.common.untils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringApplicationContext implements ApplicationContextAware {

  private static final String ERR_MSG = "Spring utility class not initialized";

  private static ApplicationContext context = null;

  @Override
  public synchronized void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    context = applicationContext;
  }

  public static <T> T bean(Class<T> tClass) {
    if (context == null) {
      throw new IllegalStateException((ERR_MSG));
    }
    return context.getBean(tClass);
  }

  public static <T> T bean(String name) {
    if (context == null) {
      throw new IllegalStateException((ERR_MSG));
    }
    return (T) context.getBean(name);
  }
}
