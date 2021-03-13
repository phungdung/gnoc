package com.viettel.gnoc.ws.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UnitDTO extends BaseDto {

  //Fields
  private String unitId;
  private String unitName;
  private String unitCode;
  private String parentUnitId;
  private String description;
  private String status;
  private String unitType;
  private String unitLevel;
  private String locationId;
  private String isNoc;
  private String timeZone;
  private String parentUnitName;
  private String parentUnitCode;
  private String userUnitId;
  private String isCommittee;
  private String updateTime;
  private String smsGatewayId;
  private String locationName;
  private String locationCode;
  private String statusStr;
  private String unitTypeStr;
  private String unitLevelStr;
  private String cdGroupUnitId;
  private String ipccServiceId;
  private String smsGatewayName;
  private String ipccServiceName;
  private String mobile;
  private String roleType;
  private String roleTypeName;
  private String email;
  private String nationId;
  private String nationCode;

  public UnitDTO(String unitId, String unitName, String unitCode, String parentUnitId,
      String description, String status, String unitType, String unitLevel, String locationId,
      String isNoc, String timeZone, String isCommittee, String updateTime, String smsGatewayId,
      String ipccServiceId, String mobile, String roleType, String email) {
    this.unitId = unitId;
    this.unitName = unitName;
    this.unitCode = unitCode;
    this.parentUnitId = parentUnitId;
    this.description = description;
    this.status = status;
    this.unitType = unitType;
    this.unitLevel = unitLevel;
    this.locationId = locationId;
    this.isNoc = isNoc;
    this.timeZone = timeZone;
    this.isCommittee = isCommittee;
    this.updateTime = updateTime;
    this.smsGatewayId = smsGatewayId;
    this.ipccServiceId = ipccServiceId;
    this.mobile = mobile;
    this.roleType = roleType;
    this.email = email;
  }

  public com.viettel.gnoc.commons.dto.UnitDTO toInsideDTO() {
    return new com.viettel.gnoc.commons.dto.UnitDTO(
        StringUtils.isStringNullOrEmpty(unitId) ? null : Long.parseLong(unitId),
        unitName,
        unitCode,
        StringUtils.isStringNullOrEmpty(parentUnitId) ? null : Long.parseLong(parentUnitId),
        description,
        StringUtils.isStringNullOrEmpty(status) ? null : Long.parseLong(status),
        StringUtils.isStringNullOrEmpty(unitType) ? null : Long.parseLong(unitType),
        StringUtils.isStringNullOrEmpty(unitLevel) ? null : Long.parseLong(unitLevel),
        StringUtils.isStringNullOrEmpty(locationId) ? null : Long.parseLong(locationId),
        StringUtils.isStringNullOrEmpty(isNoc) ? null : Long.parseLong(isNoc),
        StringUtils.isStringNullOrEmpty(timeZone) ? null : Long.parseLong(timeZone),
        parentUnitName,
        parentUnitCode,
        userUnitId,
        StringUtils.isStringNullOrEmpty(isCommittee) ? null : Long.parseLong(isCommittee),
        StringUtils.isStringNullOrEmpty(updateTime) ? null
            : DateTimeUtils.convertStringToDate(updateTime),
        StringUtils.isStringNullOrEmpty(smsGatewayId) ? null : Long.parseLong(smsGatewayId),
        locationName,
        locationCode,
        statusStr,
        unitTypeStr,
        unitLevelStr,
        cdGroupUnitId,
        StringUtils.isStringNullOrEmpty(ipccServiceId) ? null : Long.parseLong(ipccServiceId),
        smsGatewayName,
        ipccServiceName,
        mobile,
        StringUtils.isStringNullOrEmpty(roleType) ? null : Long.parseLong(roleType),
        roleTypeName,
        email,
        StringUtils.isStringNullOrEmpty(nationId) ? null : Long.parseLong(nationId),
        nationCode);
  }
}
