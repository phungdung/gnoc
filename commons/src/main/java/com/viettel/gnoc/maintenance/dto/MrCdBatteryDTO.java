package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.model.MrCdBatteryEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class MrCdBatteryDTO extends BaseDto {
  private String dcPowerId;
  private String stationId;
  private String stationCode;
  private String dcPower;
  private String province;
  private String staffName;
  private String staffMail;
  private String staffPhone;
  private String dischargeType;
  private String dischargeTypeName;
  private String timeDischarge;
  private String recentDischargeCd;
  private String mrCode;
  private String woCode;
  private String status;
  private String statusWoName;
  private String createdTime;
  private String updatedTime;
  private String updatedUser;
  private String recentDischageNoc;
  private String recentDischageGnoc;
  private String productionTechnology;
  private String districtCode;
  private String dischargeConfirm;
  private String resultDischarge;
  private String dischargeNumber;
  private String dischargeReasonFail;

  private String dcPowerIdSchedule;
  private String dateTimeSchedule;
  private String dischargeNumberSchedule;
  private String dischargeDaySchedule;
  private Integer sysHour;
  //Dunglv add
  private String recentDischargeGnoc;
  private String recentDischargeNoc;
  private String reasonAccept;
  private String statusAccept;
  private String areaCode;
  private String provinceCode;
  private String timeDischargeFrom;
  private String timeDischargeTo;

  private String provinceName;
  private String areaName;
  private String marketName;
  private String marketCode;
  private String districtName;
  private String iswoAccu;
  private String iswoAccuName;
  private String areaId;
  private String provinceId;
  private String statusWo;

  private String haveMr;

  private String UserImplemen;
  private String isImport;
  private String isEdit;

  private String statusNode;
  private String statusN;

  private Long activeStatus;
  private String activeStatusName;




//  public MrCdBatteryDTO() {
//    setDefaultSortField("dc_power_id");
//  }

  public MrCdBatteryDTO(String dcPowerId, String stationId, String stationCode, String dcPower, String province, String staffName, String staffMail, String staffPhone, String dischargeType, String timeDischarge, String recentDischargeCd, String mrCode, String woCode, String status, String createdTime, String updatedTime, String updatedUser, String recentDischageNoc, String recentDischageGnoc, String productionTechnology, String districtCode, String dischargeConfirm, String resultDischarge, String dischargeNumber, String dischargeReasonFail, String recentDischargeGnoc, String recentDischargeNoc, String reasonAccept, String statusAccept, String areaCode, String provinceCode, String iswoAccu, String marketCode, String districtName) {
    this.dcPowerId = dcPowerId;
    this.stationId = stationId;
    this.stationCode = stationCode;
    this.dcPower = dcPower;
    this.province = province;
    this.staffName = staffName;
    this.staffMail = staffMail;
    this.staffPhone = staffPhone;
    this.dischargeType = dischargeType;
    this.timeDischarge = timeDischarge;
    this.recentDischargeCd = recentDischargeCd;
    this.mrCode = mrCode;
    this.woCode = woCode;
    this.status = status;
    this.createdTime = createdTime;
    this.updatedTime = updatedTime;
    this.updatedUser = updatedUser;
    this.recentDischageNoc = recentDischageNoc;
    this.recentDischageGnoc = recentDischageGnoc;
    this.productionTechnology = productionTechnology;
    this.districtCode = districtCode;
    this.dischargeConfirm = dischargeConfirm;
    this.resultDischarge = resultDischarge;
    this.dischargeNumber = dischargeNumber;
    this.dischargeReasonFail = dischargeReasonFail;
    this.recentDischargeGnoc = recentDischargeGnoc;
    this.recentDischargeNoc = recentDischargeNoc;
    this.reasonAccept = reasonAccept;
    this.statusAccept = statusAccept;
    this.areaCode = areaCode;
    this.provinceCode = provinceCode;
    this.iswoAccu = iswoAccu;
    this.marketCode = marketCode;
    this.districtName = districtName;
  }


  public MrCdBatteryEntity toEntity() {
    try {
      MrCdBatteryEntity model = new MrCdBatteryEntity(
          StringUtils.validString(dcPowerId) ? Long.valueOf(dcPowerId) : null
          , StringUtils.validString(stationId) ? Long.valueOf(stationId) : null
          , stationCode
          , dcPower
          , province
          , staffName
          , staffMail
          , staffPhone
          , dischargeType
          , timeDischarge
          , StringUtils.validString(recentDischargeCd) ? DateTimeUtils.convertStringToDate(recentDischargeCd) : null
          , mrCode
          , woCode
          , StringUtils.validString(status) ? status : "1"
          , StringUtils.validString(createdTime) ? DateTimeUtils.convertStringToDate(createdTime) : null
          , StringUtils.validString(updatedTime) ? DateTimeUtils.convertStringToDate(updatedTime) : null
          , updatedUser
          , StringUtils.validString(recentDischageNoc) ? DateTimeUtils.convertStringToDate(recentDischageNoc) : null
          , StringUtils.validString(recentDischageGnoc) ? DateTimeUtils.convertStringToDate(recentDischageGnoc) : null
          , productionTechnology
          , districtCode
          , dischargeConfirm
          , StringUtils.validString(resultDischarge) ? Long.valueOf(resultDischarge) : 0L
          , StringUtils.validString(dischargeNumber) ? Long.valueOf(dischargeNumber) : 0L
          , dischargeReasonFail
          , recentDischargeGnoc
          , recentDischargeNoc
          , reasonAccept
          , statusAccept
          , areaCode
          , provinceCode
          , iswoAccu
          , marketCode
          , districtName
      );
      return model;
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return null;
  }
}
