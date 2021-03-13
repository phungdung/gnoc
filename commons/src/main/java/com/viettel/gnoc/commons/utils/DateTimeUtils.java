/*
 * Copyright (C) 2010 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.gnoc.commons.utils;

import com.viettel.gnoc.commons.config.TimezoneContextHolder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sondm2@viettel.com.vn
 * @version 1.0
 * @since Apr, 12, 2010
 */
@Slf4j
public final class DateTimeUtils {

  /**
   * .
   */
  public static final int CONST3 = 3;
  /**
   * .
   */
  public static final int CONST4 = 4;
  /**
   * .
   */
  public static final int CONST5 = 5;
  /**
   * .
   */
  public static final int CONST6 = 6;
  /**
   * .
   */
  public static final int CONST7 = 7;
  /**
   * .
   */
  public static final int CONST8 = 8;
  /**
   * .
   */
  public static final int CONST9 = 9;
  /**
   * .
   */
  public static final int CONST10 = 10;
  /**
   * .
   */
  public static final int CONST11 = 11;
  /**
   * .
   */
  public static final int CONST12 = 12;

  public static final String patternDateTime = "dd/MM/yyyy HH:mm";
  public static final String patternDateTimeMs = "dd/MM/yyyy HH:mm:ss";
  public static final String patternDateTimeMs00 = "dd/MM/yyyy HH:mm:00";

  /**
   * private constructor
   */
  private DateTimeUtils() {
  }

  /**
   * @param date to convert
   * @param pattern in converting
   * @return date
   */
  public static Date convertStringToTime(String date, String pattern) throws Exception {
    SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);
    try {
      return dateFormat.parse(date);

    } catch (ParseException e) {
      System.out.println("Date ParseException, string value:" + date);
      throw e;
    }
  }

  /**
   * @param date to convert
   * @return String
   * @throws Exception if error
   */
  public static Date convertStringToDate(String date) {
    String dateValue = date;
    String pattern = "dd/MM/yyyy HH:mm:ss";
    if (date.length() <= 10) {
      dateValue = date + " 00:00:00";
    }
    try {
      return convertStringToTime(dateValue, pattern);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }

  public static Date convertStringToDate1(String date) throws Exception {
    String dateValue = date;
    String pattern = "dd/MM/yyyy HH:mm:ss";
    if (date.length() <= 10) {
      dateValue = date + " 00:00:00";
    }
    return convertStringToTime1(dateValue, pattern);
  }

  public static Date convertStringToTime1(String date, String pattern) throws Exception {
    SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);
    try {
      return dateFormat.parse(date);

    } catch (ParseException e) {
      System.out.println("Date ParseException, string value:" + date);
      throw e;
    }
  }

  public static Date convertDateOffset() throws Exception {
    SimpleDateFormat spd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date date = new Date();
    Double offset = TimezoneContextHolder.getOffsetDouble();
    date = spd
        .parse(date2ddMMyyyyHHMMss(new Date(date.getTime() + (long) (offset * 60 * 60 * 1000))));
    return date;
  }

  /**
   * @param date to convert
   * @return String
   * @throws Exception if error
   */
  public static String convertDateToString(Date date) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    if (date == null) {
      return "";
    }
    try {
      return dateFormat.format(date);
    } catch (Exception e) {
      throw e;
    }
  }

  public static String convertDateToString(Date date, String pattern) {
    SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);
    if (date == null) {
      return "";
    }
    try {
      return dateFormat.format(date);
    } catch (Exception e) {
//            e.printStackTrace();
      log.error(e.getMessage(), e);
      return "";
    }
  }

  /**
   * @return String
   * @throws Exception if error
   */
  public static String getSysdate() throws Exception {
    Calendar calendar = Calendar.getInstance();
    return convertDateToString(calendar.getTime());
  }

  /**
   * @return String
   * @throws Exception if error
   */
  public static String getSysDateTime() {
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
    try {
      return dateFormat.format(calendar.getTime());
    } catch (Exception e) {
      throw e;
    }
  }

  /**
   * @param pattern to convert
   * @return String
   * @throws Exception if error
   */
  public static String getSysDateTime(String pattern) throws Exception {
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);
    try {
      return dateFormat.format(calendar.getTime());
    } catch (Exception e) {
      throw e;
    }
  }

  /**
   * @param date to convert
   * @return String
   * @throws Exception if error
   */
  public static Date convertStringToDateTime(String date) throws Exception {
    String pattern = "dd/MM/yyyy HH:mm:ss";
    return convertStringToTime(date, pattern);
  }

  /**
   * @param date to convert
   * @return String
   * @throws Exception if error
   */
  public static String convertDateTimeToString(Date date) throws Exception {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
    try {
      return dateFormat.format(date);
    } catch (Exception e) {
      throw e;
    }
  }

  /**
   * @param utilDate to convert
   * @return date
   */
  public static java.sql.Date convertToSqlDate(Date utilDate) {
    return new java.sql.Date(utilDate.getTime());
  }

  /**
   * @param monthInput to parse
   * @return String
   */
  public static String parseDate(int monthInput) {
    String dateReturn;
    Calendar cal = Calendar.getInstance();
    switch (monthInput) {
      case 1:
        dateReturn = "01/01/";
        break;
      case 2:
        dateReturn = "01/02/";
        break;
      case CONST3:
        dateReturn = "01/03/";
        break;
      case CONST4:
        dateReturn = "01/04/";
        break;
      case CONST5:
        dateReturn = "01/05/";
        break;
      case CONST6:
        dateReturn = "01/06/";
        break;
      case CONST7:
        dateReturn = "01/07/";
        break;
      case CONST8:
        dateReturn = "01/08/";
        break;
      case CONST9:
        dateReturn = "01/09/";
        break;
      case CONST10:
        dateReturn = "01/10/";
        break;
      case CONST11:
        dateReturn = "01/11/";
        break;
      case CONST12:
        dateReturn = "01/12/";
        break;
      default:
        dateReturn = "01/01/";
        break;
    }
    return dateReturn + cal.get(Calendar.YEAR);
  }

  public static int compareDateTime(Date d1, Date d2) {
    int result = 0;
    Calendar cal1 = Calendar.getInstance();
    Calendar cal2 = Calendar.getInstance();
    cal1.setTime(d1);
    cal2.setTime(d2);
    if (cal1.after(cal2)) {
      result = 1;
    } else if (cal1.before(cal2)) {
      result = -1;
    }
    return result;
  }

  public static String date2ddMMyyyyString(Date value) {
    if (value != null) {
      SimpleDateFormat ddMMyyyy = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
      return ddMMyyyy.format(value);
    }
    return "";
  }

  public static Date string2Date(String value) throws ParseException {
    if (!DateTimeUtils.isNullOrEmpty(value)) {
      SimpleDateFormat dateTime = new SimpleDateFormat(
          "dd/MM/yyyy", Locale.ENGLISH);
      return dateTime.parse(value);
    }
    return null;
  }

  public static boolean isNullOrEmpty(String obj1) {
    return (obj1 == null || "".equals(obj1.trim()));
  }

  public static String getSysdate(String format) {
//        String selectTocharSysdate = "select to_char(sysdate,'dd/mm/yyyy hh24:mi:ss') from dual";
    long time = System.currentTimeMillis();
    java.sql.Date date = new java.sql.Date(time);
    return convertDateToString(date, format);
  }

  /**
   * @param value Date
   * @return String
   */
  public static String date2StringNoSlash(Date value) {
    if (value != null) {
      SimpleDateFormat dateNoSlash = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
      return dateNoSlash.format(value);
    }
    return "";
  }

  /**
   * @param value Date
   * @return String
   */
  public static String date2ddMMyyyyHHMMss(Date value) {
    if (value != null) {
      SimpleDateFormat dateTimeNoSlash = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss",
          Locale.ENGLISH);
      return dateTimeNoSlash.format(value);
    }
    return "";
  }

  public static Date convertToDate(String value, String pattern, Double offset, Boolean isInputDate)
      throws ParseException {
    if (value != null) {
      SimpleDateFormat format = new SimpleDateFormat(
          pattern == null ? "dd/MM/yyyy HH:mm:ss" : pattern, Locale.ENGLISH);
      return convertDateToOffset(format.parse(value), offset, isInputDate);
    }
    return null;
  }

  public static Date convertDateToOffset(Date date, Double offset, Boolean isInputDate) {
    if (date == null || offset == null || offset.equals(0D)) {
      return date;
    }
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    try {
      if (isInputDate) {
        cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) - offset.intValue());
        Double timeTmp = offset - offset.intValue();
        if (timeTmp > 0 || timeTmp < 0) {
          timeTmp = timeTmp * 60;
          cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) - timeTmp.intValue());
        }
      } else {
        cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + offset.intValue());
        Double timeTmp = offset - offset.intValue();
        if (timeTmp > 0 || timeTmp < 0) {
          timeTmp = timeTmp * 60;
          cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) + timeTmp.intValue());
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return cal.getTime();
  }

  public static boolean isDate(String data, String partent) {
    try {
      SimpleDateFormat dfm = new SimpleDateFormat(partent);
      dfm.parse(data);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return false;
    }
    return true;
  }

  public static boolean isDateHH24(String data) {
    try {
      String partent = "dd/MM/yyyy HH:mm:ss";
      return isDate(data, partent);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return false;
    }
  }

  /**
   *
   */
  public static String converClientDateToServerDate(String clientTime, Double offset) {
    String result = clientTime;
    if (offset == null || offset.equals(0D)) {
      return clientTime;
    }
    if (clientTime == null || "".equals(clientTime.trim())) {
      return result;
    }
    try {
      Calendar cal = Calendar.getInstance();
      Date date = DateTimeUtils.convertStringToDate(result);
      cal.setTime(date);
      cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) - offset.intValue());
      Double timeTmp = offset - offset.intValue();
      if (timeTmp > 0 || timeTmp < 0) {
        timeTmp = timeTmp * 60;
        cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) - timeTmp.intValue());
      }
      Date dateConver = cal.getTime();
      return convertStringToDateTime(dateConver);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return result;
  }

  /**
   * @param date to convert
   * @return String
   * @throws Exception if error
   */
  public static String convertStringToDateTime(Date date) throws Exception {
    String result = "";
    try {
      String pattern = "dd/MM/yyyy HH:mm:ss";
      SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
      return sdf.format(date);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return result;
  }

  public static String convertDateTimeStampToString(Date date) {
    if (date != null) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      try {
        return dateFormat.format(date);
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String temp = dateFormat.format(date) + ":00";
        return temp;
      }
    }
    return null;
  }

  public static Timestamp convertStringToTimestamp(String strDate) {
    try {
      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      Date date = formatter.parse(strDate);
      Timestamp timeStampDate = new Timestamp(date.getTime());
      return timeStampDate;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }

  public static String convertTimestampToString(Timestamp strDate) {
    try {
      Date date = new Date(strDate.getTime());
      return date2ddMMyyyyHHMMss(date);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }

  public static String validateInput(String fromDate, String toDate) {
    String ret = Constants.RESULT.SUCCESS;
    if (isNullOrEmpty(fromDate)) {
      ret = "fromDate.isNull";
      return ret;
    }
    try {
      if (!isNullOrEmpty(fromDate)) {
        DateTimeUtils.convertStringToDateTime(fromDate);
      }
    } catch (Exception ex) {
      ret = "fromDate.invalid.dd.MM.yyyy.hh.mm.ss";
      log.error(ex.getMessage(), ex);
      return ret;
    }
    try {
      if (!isNullOrEmpty(toDate)) {
        DateTimeUtils.convertStringToDateTime(toDate);
      }
    } catch (Exception ex) {
      ret = "toDate.invalid.dd.MM.yyyy.hh.mm.ss";
      log.error(ex.getMessage(), ex);
      return ret;
    }
    return ret;
  }

  public static Date converStringClientToServerDate(String clientTime, Double offset) {
    String result = clientTime;
    try {
      Calendar cal = Calendar.getInstance();
      Date date = DateTimeUtils.convertStringToDate(result);
      cal.setTime(date);
      cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) - offset.intValue());
      Double timeTmp = offset - offset.intValue();
      if (timeTmp > 0 || timeTmp < 0) {
        timeTmp = timeTmp * 60;
        cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) - timeTmp.intValue());
      }
      return cal.getTime();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  public static boolean checkDateFormat(String date, String pattern) {
    String regex = "^(3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]{4}$";
    Pattern r = Pattern.compile(regex);
    Matcher m = r.matcher(date);
    if (!m.matches()) {
      return false;
    }
    try {
      SimpleDateFormat dm = new SimpleDateFormat(pattern);
      dm.parse(date);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return false;
    }
    return true;
  }
}
