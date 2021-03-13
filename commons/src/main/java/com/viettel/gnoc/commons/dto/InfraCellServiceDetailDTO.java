/*
 * Copyright 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.gnoc.commons.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InfraCellServiceDetailDTO extends BaseDto {

  private String btsCode;
  private String cellCode;
  private String deviceName;
  private String deviceCode;
  private String active;
  private String cellType;
}
