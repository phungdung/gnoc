package com.viettel.gnoc.cr.util;

import java.util.Locale;
import java.util.ResourceBundle;
import lombok.extern.slf4j.Slf4j;

//anhlp add
@Slf4j
public final class CMResourceBundleUtils {

  private CMResourceBundleUtils() {
    //12345
  }

  static private ResourceBundle rb = null;

  public static String getApplicationResourceByMap(String key, String locale) {
    String result = key;
    try {
      Locale localeDefault = new Locale("vi_VN");
      if (locale == null || "".equals(locale.trim())) {
        rb = ResourceBundle.getBundle("com.viettel.gnoc.cr.Languages.CMResource", localeDefault);
      } else {
        Locale localeN = new Locale(locale.trim());
        rb = ResourceBundle.getBundle("com.viettel.gnoc.cr.Languages.CMResource", localeN);
      }
      result = rb.getString(key);

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return key;
    }
    return result;
  }

  public static String getChangeManagementString(String key, String locale) {
    String result = key;
    try {
      Locale localeDefault = new Locale("vi_VN");
      if (locale == null || "".equals(locale.trim())) {
        rb = ResourceBundle.getBundle("com.viettel.gnoc.cr.Languages.changemanage", localeDefault);
      } else {
        Locale localeN = new Locale(locale.trim());
        rb = ResourceBundle.getBundle("com.viettel.gnoc.cr.Languages.changemanage", localeN);
      }
      result = rb.getString(key);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return key;
    }
    return result;
  }

  public static String getConfigResource(String key) {
    String result = key;
    try {
      rb = ResourceBundle.getBundle("com.viettel.gnoc.cr.Languages.CMConfig");
      result = rb.getString(key);

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return key;
    }
    return result;
  }
}
