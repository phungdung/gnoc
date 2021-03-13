package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.model.MrUserCfgApprovedSmsBtsEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author TrungDuong
 */
@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class MrUserCfgApprovedSmsBtsDTO extends BaseDto {

  private Long userCfgApprovedSmsId;
  private String marketCode;
  private String areaCode;
  private String provinceCode;
  private String userName;
  private String mobile;
  private String approveLevel;
  private String fullName;
  private String userID;
  private String createTime;
  private String updateTime;
  private String userUpdate;
  private String receiveMessageBD;

  //them

  private String marketLocationCode;
  private String marketName;
  private String areaName;
  private String areaId;
  private String provinceId;
  private String provinceName;
  private String preCodeStation;
  private String approveLevelStr;
  private String resultImport;
  private String keyProvinceNull;
  private String receiveMessageBDStr;

  public MrUserCfgApprovedSmsBtsDTO(Long userCfgApprovedSmsId, String marketCode, String areaCode,
      String provinceCode,
      String userName, String mobile, String approveLevel, String fullName, String userID,
      String createTime, String updateTime, String userUpdate, String receiveMessageBD) {
    this.userCfgApprovedSmsId = userCfgApprovedSmsId;
    this.marketCode = marketCode;
    this.areaCode = areaCode;
    this.provinceCode = provinceCode;
    this.userName = userName;
    this.mobile = mobile;
    this.approveLevel = approveLevel;
    this.fullName = fullName;
    this.userID = userID;
    this.createTime = createTime;
    this.updateTime = updateTime;
    this.userUpdate = userUpdate;
    this.receiveMessageBD = receiveMessageBD;
  }

  public MrUserCfgApprovedSmsBtsEntity toEntity() {
    MrUserCfgApprovedSmsBtsEntity entity = null;
    try {
      entity = new MrUserCfgApprovedSmsBtsEntity(
          userCfgApprovedSmsId,
          marketCode,
          areaCode,
          provinceCode,
          userName,
          mobile,
          !StringUtils.validString(approveLevel) ? null : Long.valueOf(approveLevel),
          fullName,
          !StringUtils.validString(userID) ? null : Long.valueOf(userID),
          !StringUtils.validString(createTime) ? null
              : DateTimeUtils.convertStringToDateTime(createTime),
          !StringUtils.validString(updateTime) ? null
              : DateTimeUtils.convertStringToDateTime(updateTime),
          userUpdate,
          !StringUtils.validString(receiveMessageBD) ? null : Long.valueOf(receiveMessageBD)
      );
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return entity;
  }
}
