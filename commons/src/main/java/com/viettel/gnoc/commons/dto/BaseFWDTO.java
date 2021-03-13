/*
 * Copyright (C) 2012 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.BaseFWModel;

/**
 * @author kdvt_binhnt22@viettel.com.vn
 * @version 1.0
 * @since May 2012
 */
public abstract class BaseFWDTO<TBO extends BaseFWModel> implements BaseFWDTOInterface<TBO> {

  @Override
  public int compareTo(BaseFWDTOInterface o) {
    return catchName().compareTo(o.catchName());
  }

}
