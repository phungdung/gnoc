package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.model.MrDeviceBtsEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author trungduong
 */
@Getter
@Setter
@NoArgsConstructor
@Slf4j
//@MultiFieldUnique(message = "{validation.MrHardCDDTO.null.unique}", clazz = MrHardCdEntity.class, uniqueFields = "countryCode,region,cdId,stationCode", idField = "hardCDId")
public class MrDeviceBtsDTO extends BaseDto {

  private Long deviceId;
  private String marketCode;
  private String provinceCode;
  private String stationCode;
  private String deviceType;
  private String userManager;
  private String updateUser;
  private String serial;
  private String fuelType;
  private String power;
  private String maintenanceTime;
  private String operationHour;
  private String areaCode;
  private String operationHourLastUpdate;
  private String producer;
  private String putStatus;
  private String inKTTS;
  private String countryName;
  private String provinceName;

  private String woCode;
  private String areaID;
  private String areaName;
  private String deviceTypeStr;
  private String putStatusStr;
  private String StrInKTTS;
  private String fuelTypeName;
  private String maintenanceTimeFrom;
  private String maintenanceTimeTo;

  public MrDeviceBtsDTO(Long deviceId, String marketCode, String provinceCode,
      String stationCode, String deviceType, String userManager, String updateUser,
      String serial, String fuelType, String power, String maintenanceTime,
      String operationHour, String areaCode, String operationHourLastUpdate, String producer,
      String putStatus, String inKTTS, String countryName, String provinceName) {
    this.deviceId = deviceId;
    this.marketCode = marketCode;
    this.provinceCode = provinceCode;
    this.stationCode = stationCode;
    this.deviceType = deviceType;
    this.userManager = userManager;
    this.updateUser = updateUser;
    this.serial = serial;
    this.fuelType = fuelType;
    this.power = power;
    this.maintenanceTime = maintenanceTime;
    this.operationHour = operationHour;
    this.areaCode = areaCode;
    this.operationHourLastUpdate = operationHourLastUpdate;
    this.producer = producer;
    this.putStatus = putStatus;
    this.inKTTS = inKTTS;
    this.countryName = countryName;
    this.provinceName = provinceName;
  }

  public MrDeviceBtsEntity toEntity() {
    MrDeviceBtsEntity model = new MrDeviceBtsEntity(
        deviceId
        , marketCode
        , provinceCode
        , stationCode
        , deviceType
        , userManager
        , updateUser
        , serial
        , fuelType
        , power
        , !StringUtils.validString(maintenanceTime) ? null
        : DateTimeUtils.convertStringToDate(maintenanceTime)
        , !StringUtils.validString(operationHour) ? null : Double.valueOf(operationHour)
        , areaCode
        , !StringUtils.validString(operationHourLastUpdate) ? null
        : Double.valueOf(operationHourLastUpdate)
        , producer
        , putStatus
        , inKTTS
        , countryName
        , provinceName
    );
    return model;
  }
}
