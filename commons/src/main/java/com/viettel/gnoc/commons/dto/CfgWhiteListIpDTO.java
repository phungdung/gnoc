/**
 * @(#)CatItemForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.CfgWhiteListIpEntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author itsol
 * @version 2.0
 * @since 12/03/2018
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CfgWhiteListIpDTO extends BaseDto {

  //Fields
  private Long id;
  private String userName;
  private String ip;
  private String systemName;
  private Date createDate;
  private Long status;

  public CfgWhiteListIpEntity toEntity() {
    return new CfgWhiteListIpEntity(id, userName, ip, systemName, createDate, status);
  }
}
