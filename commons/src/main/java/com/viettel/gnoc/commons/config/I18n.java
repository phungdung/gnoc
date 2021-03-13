package com.viettel.gnoc.commons.config;

import java.util.List;
import java.util.Locale;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.headers.Header;
import org.apache.cxf.jaxws.context.WrappedMessageContext;
import org.apache.cxf.message.Message;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.w3c.dom.Element;

/**
 * @author TungPV
 */
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
    if (strLocale != null && "en".equalsIgnoreCase(strLocale) || "en_us"
        .equalsIgnoreCase(strLocale)) {
      locale = new Locale("en", "US");
    } else if (strLocale != null && "vi".equalsIgnoreCase(strLocale) || "vi_vn"
        .equalsIgnoreCase(strLocale)) {
      locale = new Locale("vi", "VN");
    } else if (strLocale != null && "lo".equalsIgnoreCase(strLocale) || "lo_la"
        .equalsIgnoreCase(strLocale)) {
      locale = new Locale("lo", "LA");
    } else {
      locale = new Locale("vi", "VN");
    }
    LocaleContextHolder.setLocale(locale);
  }

  public static String getLocaleSplit() {
    String locale = LocaleContextHolder.getLocale().toString();
    return locale.split("_")[0];
  }

  public static Long getOffset() {
    String utcOffset = TimezoneContextHolder.getOffset();
    return Long.valueOf(utcOffset);
  }

  public static String getChangeManagement(String code, Object... objects) {
    try {
      Locale locale = LocaleContextHolder.getLocale();
      MessageSource messageSource = SpringApplicationContext.bean(MessageSource.class);
      return messageSource.getMessage("changemanage." + code, objects, locale);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return code;
    }
  }

  public static void setLocaleForService(WebServiceContext wsContext) {
    String locale = "";
    try {
      MessageContext mcc = wsContext.getMessageContext();
      Message message = ((WrappedMessageContext) mcc).getWrappedMessage();
      List<Header> headers = (List<Header>) message.get(Header.HEADER_LIST);
      for (Header h : headers) {
        Element e = (Element) h.getObject();
        if ("LOCALE".equalsIgnoreCase(e.getTagName())) {
          locale = e.getFirstChild().getTextContent();
          break;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    setLocale(locale);
  }
}
