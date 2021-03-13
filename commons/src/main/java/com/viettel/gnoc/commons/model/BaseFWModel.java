/*
 * Copyright (C) 2012 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.gnoc.commons.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author kdvt_binhnt22@viettel.com.vn
 * @version 1.0
 * @since May 2012
 */
@Getter
@Setter
public abstract class BaseFWModel implements BaseFwModelInterface {

  private transient String colId = "ID";
  private transient String colName = "NAME";
  private transient String[] uniqueColumn = new String[0];

  public String getModelName() {
    return this.getClass().getSimpleName();
  }

  public String getColId() {
    return colId;
  }

  public void setColId(String colId) {
    this.colId = colId;
  }

}
