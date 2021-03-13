/**
 * @(#)OdForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.od.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.od.model.OdHistoryEntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OdHistoryDTO extends BaseDto {

  //Fields
  private Long odHisId;
  private Long odId;
  private Date updateTime;
  private Long oldStatus;
  private Long newStatus;
  private Long userId;
  private Long unitId;
  private String content;
  private Long isSendMesssage;
  private String userName;
  private String oldStatusName;
  private String newStatusName;

  public OdHistoryDTO() {
    this.setSortName("odHisId");
  }

  public OdHistoryDTO(Long odHisId, Long odId, Date updateTime, Long oldStatus, Long newStatus,
      Long userId,
      Long unitId, String content, Long isSendMesssage, String userName) {
    this.odHisId = odHisId;
    this.odId = odId;
    this.updateTime = updateTime;
    this.oldStatus = oldStatus;
    this.newStatus = newStatus;
    this.userId = userId;
    this.unitId = unitId;
    this.content = content;
    this.isSendMesssage = isSendMesssage;
    this.userName = userName;
  }

  ;

  public OdHistoryEntity toEntity() {
    return new OdHistoryEntity(this.odHisId, this.odId, this.updateTime, this.oldStatus,
        this.newStatus, this.userId,
        this.unitId, this.content, this.isSendMesssage, this.userName);
  }
}
