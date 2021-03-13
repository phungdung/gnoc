
/*
 * Copyright (C) 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.gnoc.wfm.dto;

import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dungnv50
 * @version 1.0
 * @since 5/8/2015 4:40 PM
 */
@Getter
@Setter
@AllArgsConstructor
@Slf4j
public class WoTypeDTO {

  //Fields
  private String woTypeId;
  private String woTypeCode;
  private String woTypeName;
  private String defaultSortField;
  private String enableCreate;
  private String createFromOtherSys;

  private String cdGroupId;
  private String woGroupType;
  private String isEnable;
  private String timeOver;
  private String smsCycle;
  private String woCloseAutomaticTime;
  private String userTypeCode;
  private String allowPending;
  private String timeAutoCloseWhenOver;
  private String processTime;

  private String isOtherSys;
  private List<String> lstCdGroup;
  private String woTypeGroupId;
  private String woGroupId;

  private String isEnableName;

  public WoTypeDTO() {
    setDefaultSortField("woTypeName");
  }

  public WoTypeInsideDTO toWoTypeInsideDTO() {
    WoTypeInsideDTO model = new WoTypeInsideDTO(
        StringUtils.validString(woTypeId) ? Long.valueOf(woTypeId) : null,
        woTypeCode,
        woTypeName,
        StringUtils.validString(isEnable) ? Long.valueOf(isEnable) : null,
        StringUtils.validString(woGroupType) ? Long.valueOf(woGroupType) : null,
        StringUtils.validString(cdGroupId) ? Long.valueOf(cdGroupId) : null,
        StringUtils.validString(enableCreate) ? Long.valueOf(enableCreate) : null,
        StringUtils.validString(timeOver) ? Long.valueOf(timeOver) : null,
        StringUtils.validString(smsCycle) ? Long.valueOf(smsCycle) : null,
        woCloseAutomaticTime,
        userTypeCode,
        StringUtils.validString(allowPending) ? Long.valueOf(allowPending) : null,
        StringUtils.validString(createFromOtherSys) ? Long.valueOf(createFromOtherSys) : null,
        StringUtils.validString(timeAutoCloseWhenOver) ? Long.valueOf(timeAutoCloseWhenOver)
            : null, StringUtils.validString(processTime) ? Long.valueOf(processTime) : null,
        StringUtils.validString(isOtherSys) ? DataUtil.isBoolean(isOtherSys) : null,
        lstCdGroup,
        woTypeGroupId,
        woGroupId,
        isEnableName);
    return model;
  }

  public WoTypeDTO(String woTypeId, String woTypeCode, String woTypeName, String isEnable,
      String woGroupType, String enableCreate, String timeOver, String smsCycle,
      String woCloseAutomaticTime, String userTypeCode, String allowPending,
      String createFromOtherSys, String timeAutoCloseWhenOver, String processTime,
      String isOtherSys,
      List<String> lstCdGroup, String woTypeGroupId, String woGroupId, String isEnableName) {
    this.woTypeId = woTypeId;
    this.woTypeCode = woTypeCode;
    this.woTypeName = woTypeName;
    this.isEnable = isEnable;
    this.woGroupType = woGroupType;
    this.enableCreate = enableCreate;
    this.timeOver = timeOver;
    this.smsCycle = smsCycle;
    this.woCloseAutomaticTime = woCloseAutomaticTime;
    this.userTypeCode = userTypeCode;
    this.allowPending = allowPending;
    this.createFromOtherSys = createFromOtherSys;
    this.timeAutoCloseWhenOver = timeAutoCloseWhenOver;
    this.processTime = processTime;
    this.isOtherSys = isOtherSys;
    this.lstCdGroup = lstCdGroup;
    this.woTypeGroupId = woTypeGroupId;
    this.woGroupId = woGroupId;
    this.isEnableName = isEnableName;
    setDefaultSortField("woTypeName");
  }
}
