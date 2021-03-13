/*
 * Copyright (C) 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.gnoc.commons.utils;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author kdvt_binhnt22@viettel.com.vn
 * @version 1.0
 * @since since_text
 */
@Getter
@Setter
@NoArgsConstructor
public class ConditionBean {

  private String field;
  private String value;
  private String operator;
  private String type;

  private List<ConditionBean> conditionBeans;

  public ConditionBean(String field, String operator, String value) {
    this.field = field;
    this.value = value;
    this.operator = operator;
  }

  public ConditionBean(String field, String value, String operator, String type) {
    this.field = field;
    this.value = value;
    this.operator = operator;
    this.type = type;
  }
}
