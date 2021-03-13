/*
 * Copyright (C) 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.BaseFWModel;

/**
 * @author kdvt_binhnt22@viettel.com.vn
 * @version 1.0
 * @since since_text
 */
public interface BaseFWDTOInterface<TModel extends BaseFWModel> extends
    Comparable<BaseFWDTOInterface> {

  TModel toEntity();

  String getFWModelId();

  String catchName();

}
