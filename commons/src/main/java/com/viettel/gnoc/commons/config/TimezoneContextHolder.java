package com.viettel.gnoc.commons.config;

public class TimezoneContextHolder {

  private static final ThreadLocal<String> OFFSET = new ThreadLocal<String>() {
    @Override
    protected String initialValue() {
      return null;
    }
  };

  public static void setOffset(String offset) {
    OFFSET.set(offset);
  }

  public static String getOffset() {
    return OFFSET.get();
  }

  public static Double getOffsetDouble() {
    String value = OFFSET.get();
    if (value == null) {
      return 0d;
    }
    return Double.valueOf(OFFSET.get());
  }

  public static void removeOffset() {
    OFFSET.remove();
  }
}
