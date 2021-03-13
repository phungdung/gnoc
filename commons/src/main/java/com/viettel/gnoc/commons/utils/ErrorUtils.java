package com.viettel.gnoc.commons.utils;

public class ErrorUtils {

  protected static org.apache.log4j.Logger logger = org.apache.log4j.Logger
      .getLogger(ErrorUtils.class);

  private ErrorUtils() {
  }

  public static String printLog(Exception e) {
    String str;
    try {
      str = new StringBuilder().append(":").append(e.toString()).toString();
      if ((e.getCause() != null) && (e.getCause().getMessage() != null)) {
        str = new StringBuilder().append(str).append(" - Caused by ")
            .append(e.getCause().getMessage()).toString();
      }
      str = new StringBuilder().append(str).append("\n").toString();
      StackTraceElement[] traceList = e.getStackTrace();
      for (StackTraceElement trace : traceList) {
        if (trace.getClassName().contains("com.viettel.gnoc")
            || trace.getClassName().contains("com.viettel.vfw5")
            || trace.getClassName().contains("com.viettel.vwf5")) {
          str = new StringBuilder().append(str).append(" [").append(trace.getClassName())
              .append(".class][").append(trace.getMethodName()).append("][")
              .append(trace.getLineNumber()).append("]\n").toString();
        }
      }
    } catch (Exception ex) {
      logger.info(ex.getMessage(), ex);
      str = new StringBuilder().append(":").append(e.toString()).toString();
    }
    return str;
  }

  public static void printError(Exception e, Throwable ex) {
    logger.error(printLog(e), ex);
  }

}
