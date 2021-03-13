package com.viettel.gnoc.commons.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.security.PassTranformer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;

/**
 * @author TungPV
 */
@Slf4j
public class StringUtils {

  public static final String BLANK_STRING_VALUE = "";

  public static boolean isStringNullOrEmpty(Object obj1) {
    return obj1 == null || BLANK_STRING_VALUE.equals(obj1.toString().trim());
  }

  public static boolean isNotNullOrEmpty(String obj1) {
    return obj1 != null && !BLANK_STRING_VALUE.equals(obj1.trim());
  }

  public static List<?> stringToArray(Class<?> persistentClass, String value, String character) {
    List<String> listString = new ArrayList<>(Arrays.asList(value.split(character)));
    if (persistentClass.equals(String.class)) {
      return listString;
    }
    if (persistentClass.equals(Long.class)) {
      List<Long> listLong = new ArrayList<>();
      for (String string : listString) {
        listLong.add(Long.parseLong(string));
      }
      return listLong;
    }
    return listString;
  }

  public static boolean isInteger(String str) {
    if (str == null || !str.matches("[0-9]+$")) {
      return false;
    }
    return true;
  }

  public static boolean isLong(String str) {
    try {
      Long.valueOf(str);
      return true;
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      return false;
    }
  }

  public static String convertLowerParamContains(String value) {
    String result = value.trim().toLowerCase()
        .replace("\\", "\\\\")
        .replaceAll("%", "\\\\%")
        .replaceAll("_", "\\\\_");
    return "%" + result + "%";
  }

  public static String convertUpperParamContains(String value) {
    String result = value.trim().toUpperCase()
        .replace("\\", "\\\\")
        .replaceAll("%", "\\\\%")
        .replaceAll("_", "\\\\_");
    return "%" + result + "%";
  }

  public static String removeSeparator(String pathInput) {
    String path = pathInput;
    path = path.replace("\\\\", "\\").replace("//", "/");
    path = path.replace("\\/", "/").replace("/\\", "/");
    return path;
  }

  public static boolean validString(Object temp) {
    if (temp == null || "".equals(temp.toString().trim())) {
      return false;
    }
    return true;
  }

  public static String getStringParttern(String input, String pattern) {
    try {
      Pattern patterns = Pattern.compile(pattern);
      Matcher matcher = patterns.matcher(input);
      if (matcher.find()) {
        return matcher.group(0);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

    return input;
  }

  public static String formatLike(String str) {
    return "%" + str.trim().toLowerCase().replace("\\", "\\\\").replaceAll("%", "\\\\%")
        .replaceAll("_", "\\\\_") + "%";
  }

  public static String formatFunction(String function, String str) {
    return " " + function + "(" + str + ") ";
  }

  public static String formatDate(Date date) {
//        return " to_date('" + DateTimeUtils.convertDateToString(date, ParamUtils.ddMMyyyy) + "', '" + ParamUtils.ddMMyyyy + "')";
    return DateTimeUtils.convertDateToString(date, Constants.formatterDateText);
  }

  public static String formatOrder(String str, String direction) {
    return " NLSSORT(" + str + ",'NLS_SORT=vietnamese') " + direction;
  }

  //Convert operator type
  public static String convertTypeOperator(String operator) {
    String opConvert = "";
    if (StringUtils.isStringNullOrEmpty(operator)) {
      return opConvert;
    }
    switch (operator) {
      case Constants.NAME_EQUAL:
        opConvert = Constants.OPERATOR_SQL.OP_EQUAL;
        break;
      case Constants.NAME_GREATER:
        opConvert = Constants.OPERATOR_SQL.OP_GREATER;
        break;
      case Constants.NAME_GREATER_EQUAL:
        opConvert = Constants.OPERATOR_SQL.OP_GREATER_EQUAL;
        break;
      case Constants.NAME_LESS:
        opConvert = Constants.OPERATOR_SQL.OP_LESS;
        break;
      case Constants.NAME_LESS_EQUAL:
        opConvert = Constants.OPERATOR_SQL.OP_LESS_EQUAL;
        break;
      case Constants.NAME_NOT_EQUAL:
        opConvert = Constants.OPERATOR_SQL.OP_NOT_EQUAL;
        break;
      case Constants.NAME_LIKE:
        opConvert = Constants.OPERATOR_SQL.OP_LIKE;
        break;
      case Constants.NAME_IN:
        opConvert = Constants.OPERATOR_SQL.OP_IN;
        break;
    }
    return opConvert;
  }

  public static boolean checkMaxlength(Long maxlength, String str) {
    if (str != null && str.trim().length() < maxlength) {
      return true;
    }
    return false;
  }

  public static boolean isLongNullOrEmpty(Long obj1) {
    return (obj1 == null || "0L".equals(obj1));
  }

  public static boolean isDoubleNullOrEmpty(Double obj1) {
    return (obj1 == null || "0D".equals(obj1));
  }

  public static String convertKeyToValueByMap(Map<Long, String> map, String key) {
    String priStr = "";
    try {
      if (key != null) {
        priStr = key.toString();
        Long priLong = Long.valueOf(key);
        if (map.containsKey(priLong)) {
          priStr = I18n.getChangeManagement(map.get(priLong));
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return priStr;
  }

  public static void escapeHTMLString(List escapeObjectList) {
    Object obj;
    for (int i = 0; i < escapeObjectList.size(); i++) {
      obj = escapeObjectList.get(i);
      escapeHTMLString(obj);
      escapeObjectList.set(i, obj);
    }
  }

  public static void escapeHTMLString(Object escapeObject) {
    String oldData;
    String newData;
    try {
      if (escapeObject != null) {
        Class escapeClass = escapeObject.getClass();
        Field fields[] = escapeClass.getDeclaredFields();
        Field superFields[] = escapeClass.getSuperclass().getDeclaredFields();
        Field allField[] = new Field[fields.length + superFields.length];
        System.arraycopy(fields, 0, allField, 0, fields.length);
        System.arraycopy(superFields, 0, allField, fields.length, superFields.length);
        for (Field f : allField) {
          f.setAccessible(true);
          if (f.getType().equals(java.lang.String.class)) {
            if (f.get(escapeObject) != null) {
              oldData = f.get(escapeObject).toString();
              newData = StringEscapeUtils.escapeSql(oldData);
              f.set(escapeObject, newData);
            }
          } else if (f.getType().isArray()) {
            if (f.getType().getComponentType().equals(java.lang.String.class)) {
              String[] tmpArr = (String[]) f.get(escapeObject);
              if (tmpArr != null) {
                for (int i = 0; i < tmpArr.length; i++) {
                  tmpArr[i] = StringEscapeUtils.escapeSql(tmpArr[i]);
                }
                f.set(escapeObject, tmpArr);
              }
            }
          } else if (f.get(escapeObject) instanceof List) {
            List<Object> tmpList = (List<Object>) f.get(escapeObject);
            for (int i = 0; i < tmpList.size(); i++) {
              if (tmpList.get(i) instanceof java.lang.String) {
                tmpList.set(i, StringEscapeUtils.escapeSql(tmpList.get(i).toString()));
              }
            }
            f.set(escapeObject, tmpList);
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  public static String removeDotVersion(String inp) throws Exception {
    String ret = inp.trim();
    try {
      if (ret != null && ret.contains(".")) {
        ret = ret.substring(0, ret.indexOf(".")) + "."
            + ret.substring(ret.indexOf("."), ret.length()).replace(".", "");
      }
    } catch (Exception e) {
      throw e;
    }
    return ret;
  }

  public static String replaceSpecicalChracterSQL(String str) {
//        return str.trim()
//                .replaceAll("%", "\\\\%")
//                .replaceAll("_", "\\\\_");
//        return str.trim().replace("\\", "\\\\").replaceAll("%", "\\%").replaceAll("_", "\\_");

    return str.trim().replace("\\", "\\\\").replaceAll("%", "\\\\%").replaceAll("_", "\\\\_");
  }

  public static void printLogData(String msg, Object object, Class<?> T) {
    try {
//      InetAddress address = InetAddress.getLocalHost();
      ObjectMapper mapper = new ObjectMapper();
      String json = mapper.writeValueAsString(object);
      log.info("---COME PRINT LOGS---");
      log.info("\n Header Request: \n" +
          "\n Host Address: " +
//      address.getHostAddress() +
          "\n Host Name: " +
//          address.getHostName() +
          "\n Request to " + msg + " : {} \n " + json);

      log.info("printLogData ---" + msg);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  public static String stringDecrypt(String input, String salt) {
    String outPut = null;
    try {
      PassTranformer.setInputKey(salt);
      outPut = PassTranformer.decrypt(input);
      PassTranformer.setInputKey(null);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      outPut = e.getMessage();
    }
    return outPut;
  }
}
