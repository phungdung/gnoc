/**
 * @(#)WoCdGroupForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ITSOL
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class WoCdGroupDTO {

  //Fields
  private String woGroupId;
  private String woGroupCode;
  private String woGroupName;
  private String email;
  private String mobile;
  private String groupTypeId;
  private String isEnable;
  private String defaultSortField;


  public WoCdGroupInsideDTO toDtoInside() {
    WoCdGroupInsideDTO model = new WoCdGroupInsideDTO(
        StringUtils.validString(woGroupId) ? Long.valueOf(woGroupId) : null,
        woGroupCode,
        woGroupName,
        email,
        mobile,
        StringUtils.validString(groupTypeId) ? Long.valueOf(groupTypeId) : null,
        StringUtils.validString(isEnable) ? Long.valueOf(isEnable) : null
    );
    return model;
  }

}
