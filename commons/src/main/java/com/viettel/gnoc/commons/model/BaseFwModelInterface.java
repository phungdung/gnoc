/*
 * Copyright (C) 2012 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.gnoc.commons.model;

import com.viettel.gnoc.commons.dto.BaseFWDTO;

/**
 * @author kdvt_binhnt22@viettel.com.vn
 * @version 1.0
 * @since May 2012
 */
public interface BaseFwModelInterface<TDTO extends BaseFWDTO> extends java.io.Serializable {

  public TDTO toDTO();
}
