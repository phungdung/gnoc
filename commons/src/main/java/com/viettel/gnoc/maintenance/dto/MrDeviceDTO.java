package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.maintenance.model.MrDeviceEntity;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class MrDeviceDTO extends BaseDto implements Cloneable {

  private Long deviceId;
  private String nodeCode;
  private String nodeIp;
  private Long cdId;
  private Long cdIdHard;
  @NotNull(message = "validation.mrDeviceDTO.createUserSoft.notNull")
  private String createUserSoft;
  private String createMr;
  private String impactNode;
  private Long numberOfCr;
  private String mrHard;
  private String mr12M;
  private String comments;
  @NotNull(message = "validation.mrDeviceDTO.mrSoft.notNull")
  private String mrSoft;
  private Date lastDate;
  private String arrayCode;
  private String deviceType;
  private String groupCode;
  private Date updateDate;
  private String updateUser;
  private String stationCode;
  private String marketCode;
  @NotNull(message = "validation.mrDeviceDTO.regionSoft.notNull")
  private String regionSoft;
  private Long isComplete1m;
  private Long isComplete3m;
  private Long isComplete6m;
  private Long isComplete12m;
  private Long isCompleteSoft;
  private Date lastDate1m;
  private Date lastDate3m;
  private Date lastDate6m;
  private Date lastDate12m;
  private String deviceName;
  private String groupId;
  private String vendor;
  private String networkType;
  private String networkClass;
  private String mrConfirmHard;
  private String mrConfirmSoft;
  private String status;
  private Date dateIntegrated;
  private String createUserHard;
  private String regionHard;
  private String userMrHard;
  //add new
  private Long boUnitSoft;
  private Long approveStatusSoft;
  private String approveReasonSoft;
  private Long boUnitHard;
  private Long approveStatusHard;
  private String approveReasonHard;

  private String implementUnit;
  private String checkingUnit;
  private String importRecordNo;
  private String cycle;
  private String mrContent;
  private String mrType;
  private String isCompleteHard;
  private String marketName;
  private String arrayCodeStr;
  private String networkTypeStr;
  private String statusStr;
  private String mrConfirmHardStr;
  private String deviceIdStr;
  private String mrHardStr;
  private String mrTypeStr;
  private String implementUnitName;
  private String implementUnitId;
  private String checkingUnitName;
  private String checkingUnitId;
  private String cdIdName;
  private String cdIdHardName;
  private String dateIntegratedStr;
  private String mrSoftDisplay;
  private String mrConfirmSoftDisplay;
  private Boolean checkNode;
  private String resultImport;
  private String deviceTypeStr;
  private String boUnitSoftName;
  private String approveStatusSoftName;
  private Long checkApprove;
  private Long checkTP;
  private String statusName;
  private String arrayName;
  private String createUserName;
  private String boUnitSoftId;
  private String numberOfCrStr;
  private String cdIdHardConfigStr;
  private String boUnitHardName;
  private String approveStatusHardName;

  private Long userIdLogin;
  private String userNameLogin;
  private Long unitIdLogin;

  private MrDeviceDTO mrDeviceOld;

  public MrDeviceDTO(Long deviceId, String nodeCode, String nodeIp, Long cdId,
      Long cdIdHard, String createUserSoft, String createMr, String impactNode,
      Long numberOfCr, String mrHard, String mr12M, String comments, String mrSoft,
      Date lastDate, String arrayCode, String deviceType, String groupCode,
      Date updateDate, String updateUser, String stationCode, String marketCode,
      String regionSoft, Long isComplete1m, Long isComplete3m, Long isComplete6m,
      Long isComplete12m, Long isCompleteSoft, Date lastDate1m, Date lastDate3m,
      Date lastDate6m, Date lastDate12m, String deviceName,
      String groupId, String vendor, String networkType, String networkClass,
      String mrConfirmHard, String mrConfirmSoft, String status, Date dateIntegrated,
      String createUserHard, String regionHard, String userMrHard) {
    this.deviceId = deviceId;
    this.nodeCode = nodeCode;
    this.nodeIp = nodeIp;
    this.cdId = cdId;
    this.cdIdHard = cdIdHard;
    this.createUserSoft = createUserSoft;
    this.createMr = createMr;
    this.impactNode = impactNode;
    this.numberOfCr = numberOfCr;
    this.mrHard = mrHard;
    this.mr12M = mr12M;
    this.comments = comments;
    this.mrSoft = mrSoft;
    this.lastDate = lastDate;
    this.arrayCode = arrayCode;
    this.deviceType = deviceType;
    this.groupCode = groupCode;
    this.updateDate = updateDate;
    this.updateUser = updateUser;
    this.stationCode = stationCode;
    this.marketCode = marketCode;
    this.regionSoft = regionSoft;
    this.isComplete1m = isComplete1m;
    this.isComplete3m = isComplete3m;
    this.isComplete6m = isComplete6m;
    this.isComplete12m = isComplete12m;
    this.isCompleteSoft = isCompleteSoft;
    this.lastDate1m = lastDate1m;
    this.lastDate3m = lastDate3m;
    this.lastDate6m = lastDate6m;
    this.lastDate12m = lastDate12m;
    this.deviceName = deviceName;
    this.groupId = groupId;
    this.vendor = vendor;
    this.networkType = networkType;
    this.networkClass = networkClass;
    this.mrConfirmHard = mrConfirmHard;
    this.mrConfirmSoft = mrConfirmSoft;
    this.status = status;
    this.dateIntegrated = dateIntegrated;
    this.createUserHard = createUserHard;
    this.regionHard = regionHard;
    this.userMrHard = userMrHard;
  }

  public MrDeviceDTO(Long deviceId, String nodeCode, String nodeIp, Long cdId,
      Long cdIdHard, String createUserSoft, String createMr, String impactNode,
      Long numberOfCr, String mrHard, String mr12M, String comments, String mrSoft,
      Date lastDate, String arrayCode, String deviceType, String groupCode,
      Date updateDate, String updateUser, String stationCode, String marketCode,
      String regionSoft, Long isComplete1m, Long isComplete3m, Long isComplete6m,
      Long isComplete12m, Long isCompleteSoft, Date lastDate1m, Date lastDate3m,
      Date lastDate6m, Date lastDate12m, String deviceName,
      String groupId, String vendor, String networkType, String networkClass,
      String mrConfirmHard, String mrConfirmSoft, String status, Date dateIntegrated,
      String createUserHard, String regionHard, String userMrHard, Long boUnitSoft,
      Long approveStatusSoft, String approveReasonSoft, Long boUnitHard,
      Long approveStatusHard, String approveReasonHard) {
    this.deviceId = deviceId;
    this.nodeCode = nodeCode;
    this.nodeIp = nodeIp;
    this.cdId = cdId;
    this.cdIdHard = cdIdHard;
    this.createUserSoft = createUserSoft;
    this.createMr = createMr;
    this.impactNode = impactNode;
    this.numberOfCr = numberOfCr;
    this.mrHard = mrHard;
    this.mr12M = mr12M;
    this.comments = comments;
    this.mrSoft = mrSoft;
    this.lastDate = lastDate;
    this.arrayCode = arrayCode;
    this.deviceType = deviceType;
    this.groupCode = groupCode;
    this.updateDate = updateDate;
    this.updateUser = updateUser;
    this.stationCode = stationCode;
    this.marketCode = marketCode;
    this.regionSoft = regionSoft;
    this.isComplete1m = isComplete1m;
    this.isComplete3m = isComplete3m;
    this.isComplete6m = isComplete6m;
    this.isComplete12m = isComplete12m;
    this.isCompleteSoft = isCompleteSoft;
    this.lastDate1m = lastDate1m;
    this.lastDate3m = lastDate3m;
    this.lastDate6m = lastDate6m;
    this.lastDate12m = lastDate12m;
    this.deviceName = deviceName;
    this.groupId = groupId;
    this.vendor = vendor;
    this.networkType = networkType;
    this.networkClass = networkClass;
    this.mrConfirmHard = mrConfirmHard;
    this.mrConfirmSoft = mrConfirmSoft;
    this.status = status;
    this.dateIntegrated = dateIntegrated;
    this.createUserHard = createUserHard;
    this.regionHard = regionHard;
    this.userMrHard = userMrHard;
    this.boUnitSoft = boUnitSoft;
    this.approveStatusSoft = approveStatusSoft;
    this.approveReasonSoft = approveReasonSoft;
    this.boUnitHard = boUnitHard;
    this.approveStatusHard = approveStatusHard;
    this.approveReasonHard = approveReasonHard;
  }

  public MrDeviceEntity toEntity() {
    return new MrDeviceEntity(deviceId, nodeCode, nodeIp, cdId, cdIdHard, createUserSoft, createMr,
        impactNode, numberOfCr, mrHard, mr12M, comments, mrSoft, lastDate, arrayCode, deviceType,
        groupCode, updateDate, updateUser, stationCode, marketCode, regionSoft, isComplete1m,
        isComplete3m, isComplete6m, isComplete12m, isCompleteSoft, lastDate1m, lastDate3m,
        lastDate6m, lastDate12m, deviceName, groupId, vendor, networkType, networkClass,
        mrConfirmHard, mrConfirmSoft, status, dateIntegrated, createUserHard, regionHard,
        userMrHard, boUnitSoft, approveStatusSoft, approveReasonSoft, boUnitHard, approveStatusHard,
        approveReasonHard);
  }

  @Override
  public MrDeviceDTO clone() {
    try {
      return (MrDeviceDTO) super.clone();
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      return null;
    }
  }
}
