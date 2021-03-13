package com.viettel.gnoc.commons.config;

public class TokenContextHolder {

  private static final ThreadLocal<String> TOKEN = new ThreadLocal<String>() {
    @Override
    protected String initialValue() {
      return null;
    }
  };

  public static void setToken(String token) {
    TOKEN.set(token);
  }

  public static String getToken() {
    return TOKEN.get();
  }

  public static void removeToken() {
    TOKEN.remove();
  }
}
