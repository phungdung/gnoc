package com.viettel.gnoc.commons.utils;

import com.google.common.base.Splitter;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class ConditionBeanUtil {

  public static void sysToOwnListCondition(List<ConditionBean> lstCondition) {
    for (ConditionBean con : lstCondition) {
      if (con.getType().equalsIgnoreCase(Constants.TYPE_DATE)) {
        con.setField(StringUtils.formatFunction("trunc", con.getField()));
      } else if (con.getType().equalsIgnoreCase(Constants.NUMBER)) {
        con.setType(Constants.TYPE_NUMBER);
      } else if (con.getType().equalsIgnoreCase(Constants.NUMBER_DOUBLE)) {
        con.setType(Constants.NUMBER_DOUBLE);
      } else {
        String value;
        if (con.getOperator().equalsIgnoreCase(Constants.NAME_LIKE)) {
          value = StringUtils.formatLike(con.getValue());
        } else {
          value = con.getValue();
        }
        con.setValue(value.toLowerCase());
        con.setField(StringUtils.formatFunction("lower", con.getField()));
      }
      con.setOperator(StringUtils.convertTypeOperator(con.getOperator()));
    }
  }

  public static void buildConditionQuery(StringBuilder sql, List<ConditionBean> lstCondition) {
    if (lstCondition != null) {
      int index = 0;
      for (ConditionBean con : lstCondition) {
        sql.append(Constants.OPERATOR_SQL.LOGIC_AND);
        sql.append(con.getField());
        sql.append(con.getOperator());
        if (con.getType().equals(Constants.STRING)) {
          sql.append(":idx").append(index++);
          if (con.getOperator().equals(Constants.OPERATOR_SQL.OP_LIKE)) {
            sql.append(" ESCAPE '\\' ");
          }
        } else if (con.getType().equals(Constants.TYPE_DATE)) {
          sql.append(" to_date(:idx").append(index++)
              .append(", '").append(Constants.formatterDateText).append("')");
        } else if (!con.getOperator().equals(Constants.OPERATOR_SQL.OP_IN)) {
          sql.append(" :idx").append(index++);
        } else {
          sql.append("( :idx").append(index++).append(" )");
        }
      }
    }
  }

  public static void fillConditionQuery(Query query, List<ConditionBean> lstCondition) {
    int index = 0;
    for (ConditionBean con : lstCondition) {
      if (con.getType().equals(Constants.TYPE_NUMBER)) {
        if (!con.getOperator().equals(Constants.OPERATOR_SQL.OP_IN)) {
          query.setParameter("idx" + index++, Long.parseLong(con.getValue()));
        } else {
          query.setParameter("idx" + index++, parseInputListLong(con.getValue()));
        }

      } else if (con.getType().equals(Constants.NUMBER_DOUBLE)) {
        if (!con.getOperator().equals(Constants.OPERATOR_SQL.OP_IN)) {
          query.setParameter("idx" + index++, Double.parseDouble(con.getValue()));
        } else {
          query.setParameter("idx" + index++, parseInputListDouble(con.getValue()));
        }
      } else if (con.getType().equals(Constants.STRING)) {
        if (con.getOperator().equals(Constants.OPERATOR_SQL.OP_IN)) {
          query.setParameter("idx" + index++, parseInputListString(con.getValue()));
        } else {
          query.setParameter("idx" + index++, con.getValue());
        }
      } else {
        query.setParameter("idx" + index++, con.getValue());
      }
    }
  }

  public static List<Long> parseInputListLong(String input) {
    List<String> lstString = parseInputList(input);
    List<Long> lstLong = new ArrayList<>();
    for (int i = 0; i < lstString.size(); i++) {
      lstLong.add(Long.parseLong(lstString.get(i)));
    }
    return lstLong;
  }

  public static List<Double> parseInputListDouble(String input) {
    List<String> lstString = parseInputList(input);
    List<Double> lstDouble = new ArrayList<>();
    for (int i = 0; i < lstString.size(); i++) {
      lstDouble.add(Double.parseDouble(lstString.get(i)));
    }
    return lstDouble;
  }

  public static List<String> parseInputListString(String input) {
    return parseInputList(input);

  }

  public static List<String> parseInputList(String input) {
    return Splitter.on(",").trimResults().omitEmptyStrings().splitToList(input);
  }
}
