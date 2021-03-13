package com.viettel.gnoc.commons.config;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class Internationalization implements WebMvcConfigurer {

  @Bean
  public LocaleResolver localeResolver() {
    CookieLocaleResolver resolver = new CookieLocaleResolver();
    resolver.setCookieDomain("app-locale-cookie");
    resolver.setDefaultLocale(new Locale("vi_VN"));
    // 60 minutes
    resolver.setCookieMaxAge(60 * 60);
    return resolver;
  }

  @Bean(name = "messageSource")
  public MessageSource getMessageResource() {
    ReloadableResourceBundleMessageSource messageResource = new ReloadableResourceBundleMessageSource();
    messageResource.setBasenames("classpath:i18n/language", "classpath:i18n/messages",
        "classpath:i18n/validation", "classpath:i18n/changemanage");
    messageResource.setDefaultEncoding("UTF-8");
    return messageResource;
  }

  @Bean
  public LocalValidatorFactoryBean validatorFactoryBean(MessageSource messageSource) {
    LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
    validatorFactoryBean.setValidationMessageSource(messageSource);
    return validatorFactoryBean;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    LocaleChangeInterceptor localeInterceptor = new LocaleChangeInterceptor();
    localeInterceptor.setParamName("language");
    registry.addInterceptor(localeInterceptor).addPathPatterns("/**");

    CustomChangeInterceptor customChangeInterceptor = new CustomChangeInterceptor();
    customChangeInterceptor.setTimeZoneName("utcOffset");
    customChangeInterceptor.setTimeZoneValue("0");
    customChangeInterceptor.setTokenName("token");
    customChangeInterceptor.setTokenValue("");
    registry.addInterceptor(customChangeInterceptor).addPathPatterns("/**");
  }
}
