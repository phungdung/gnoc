/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.model.MrDeviceCDEntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tiennv
 */
@Getter
@Setter
@AllArgsConstructor
@Slf4j
public class MrDeviceCDDTO extends BaseDto {

  private String marketCode;
  private String marketName;
  private String deviceCdId;
  private String deviceType;
  private String deviceName;
  private String stationCode;
  private String userMrHard;
  private String status;
  private String updateDate;
  private String updateUser;
  private String defaultSortField;
  private String statusDisplay;

  private Date lastDate1M;

  private Date lastDate3M;

  private Date lastDate6M;

  private Date lastDate12M;

  public MrDeviceCDDTO() {
    setDefaultSortField("deviceCdId");
  }

  public MrDeviceCDDTO(String deviceCdId, String deviceType, String deviceName, String stationCode,
      String userMrHard, String status, String updateDate, String updateUser, String marketCode,
      Date lastDate1M, Date lastDate3M, Date lastDate6M, Date lastDate12M
  ) {
    this.deviceCdId = deviceCdId;
    this.deviceType = deviceType;
    this.deviceName = deviceName;
    this.stationCode = stationCode;
    this.userMrHard = userMrHard;
    this.status = status;
    this.updateDate = updateDate;
    this.updateUser = updateUser;
    this.marketCode = marketCode;
    this.lastDate1M = lastDate1M;
    this.lastDate3M = lastDate3M;
    this.lastDate6M = lastDate6M;
    this.lastDate12M = lastDate12M;
  }

  public MrDeviceCDEntity toEntity() {
    try {
      MrDeviceCDEntity mop = new MrDeviceCDEntity(
          StringUtils.validString(deviceCdId) ? Long.valueOf(deviceCdId) : null,
          deviceType,
          deviceName,
          stationCode,
          userMrHard,
          StringUtils.validString(status) ? Long.valueOf(status) : null,
          StringUtils.validString(updateDate) ? DateTimeUtils.convertStringToDate(updateDate)
              : null,
          updateUser,
          marketCode,
          this.lastDate1M,
          this.lastDate3M,
          this.lastDate6M,
          this.lastDate12M
      );
      return mop;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }


}
