/**
 * @(#)UnitForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.UnitEntity;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.validator.Unique;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Unique(message = "{validation.UnitCommon.null.unique}", clazz = UnitEntity.class, uniqueField = "unitCode", idField = "unitId")
public class UnitDTO extends BaseDto {

  //Fields
  private Long unitId;
  @NotNull(message = "validation.UnitDTO.unitName.NotNull")
  @Size(max = 200, message = "validation.UnitDTO.unitName.tooLong")
  private String unitName;
  @NotNull(message = "validation.UnitDTO.unitCode.NotNull")
  @Size(max = 200, message = "validation.UnitDTO.unitCode.tooLong")
  private String unitCode;
  private Long parentUnitId;
  private String description;
  private Long status;
  private Long unitType;
  private Long unitLevel;
  private Long locationId;
  private Long isNoc;
  private Long timeZone;
  private Long isCommittee;
  private Date updateTime;
  private Long smsGatewayId;
  private Long ipccServiceId;
  private String parentUnitName;
  private String parentUnitCode;
  private String userUnitId;
  private String locationName;
  private String locationCode;
  private String statusStr;
  private String unitTypeStr;
  private String unitLevelStr;
  private String cdGroupUnitId;
  private String mobile;
  private Long roleType;
  private String email;
  private String nationCode;
  private Long nationId;
  private String roleTypeName;
  private String resultImport;
  private String ipccServiceName;
  private String action;
  private String smsGatewayName;
  private String isNocName;

  private String unitTrueName;
  private List<String> lstUnitIds;

  public UnitEntity toEntity() {
    return new UnitEntity(unitId, unitName, unitCode, parentUnitId, description, status,
        unitType, unitLevel, locationId, isNoc, timeZone, isCommittee, updateTime, smsGatewayId,
        ipccServiceId, nationCode, nationId, mobile, roleType, email);
  }

  public UnitDTO(Long unitId, String unitName, String unitCode, Long parentUnitId,
      String description, Long status, Long unitType, Long unitLevel, Long locationId,
      Long isNoc, Long timeZone, Long isCommittee, Date updateTime, Long smsGatewayId,
      Long ipccServiceId, String nationCode, Long nationId, String mobile, Long roleType,
      String email) {
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
    this.nationCode = nationCode;
    this.nationId = nationId;
    this.mobile = mobile;
    this.roleType = roleType;
    this.email = email;
  }

  public UnitDTO(Long unitId,
      String unitName,
      String unitCode,
      Long parentUnitId,
      String description,
      Long status,
      Long unitType,
      Long unitLevel,
      Long locationId,
      Long isNoc,
      Long timeZone,
      String parentUnitName,
      String parentUnitCode,
      String userUnitId,
      Long isCommittee,
      Date updateTime,
      Long smsGatewayId,
      String locationName,
      String locationCode,
      String statusStr,
      String unitTypeStr,
      String unitLevelStr,
      String cdGroupUnitId,
      Long ipccServiceId,
      String smsGatewayName,
      String ipccServiceName,
      String mobile,
      Long roleType,
      String roleTypeName,
      String email,
      Long nationId,
      String nationCode) {
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
    this.parentUnitName = parentUnitName;
    this.parentUnitCode = parentUnitCode;
    this.userUnitId = userUnitId;
    this.isCommittee = isCommittee;
    this.updateTime = updateTime;
    this.smsGatewayId = smsGatewayId;
    this.locationName = locationName;
    this.locationCode = locationCode;
    this.statusStr = statusStr;
    this.unitTypeStr = unitTypeStr;
    this.unitLevelStr = unitLevelStr;
    this.cdGroupUnitId = cdGroupUnitId;
    this.ipccServiceId = ipccServiceId;
    this.smsGatewayName = smsGatewayName;
    this.ipccServiceName = ipccServiceName;
    this.mobile = mobile;
    this.roleType = roleType;
    this.roleTypeName = roleTypeName;
    this.email = email;
    this.nationId = nationId;
    this.nationCode = nationCode;
  }

  public com.viettel.gnoc.ws.dto.UnitDTO toOutsideDTO() {
    return new com.viettel.gnoc.ws.dto.UnitDTO(
        StringUtils.isLongNullOrEmpty(unitId) ? null : unitId.toString(),
        unitName,
        unitCode,
        StringUtils.isLongNullOrEmpty(parentUnitId) ? null : parentUnitId.toString(),
        description,
        StringUtils.isLongNullOrEmpty(status) ? null : status.toString(),
        StringUtils.isLongNullOrEmpty(unitType) ? null : unitType.toString(),
        StringUtils.isLongNullOrEmpty(unitLevel) ? null : unitLevel.toString(),
        StringUtils.isLongNullOrEmpty(locationId) ? null : locationId.toString(),
        StringUtils.isLongNullOrEmpty(isNoc) ? null : isNoc.toString(),
        StringUtils.isLongNullOrEmpty(timeZone) ? null : timeZone.toString(),
        parentUnitName,
        parentUnitCode,
        userUnitId,
        StringUtils.isLongNullOrEmpty(isCommittee) ? null : isCommittee.toString(),
        StringUtils.isStringNullOrEmpty(updateTime) ? null
            : DateTimeUtils.date2ddMMyyyyHHMMss(updateTime),
        StringUtils.isLongNullOrEmpty(smsGatewayId) ? null : smsGatewayId.toString(),
        locationName,
        locationCode,
        statusStr,
        unitTypeStr,
        unitLevelStr,
        cdGroupUnitId,
        StringUtils.isLongNullOrEmpty(ipccServiceId) ? null : ipccServiceId.toString(),
        smsGatewayName,
        ipccServiceName,
        mobile,
        StringUtils.isLongNullOrEmpty(roleType) ? null : roleType.toString(),
        roleTypeName,
        email,
        StringUtils.isLongNullOrEmpty(nationId) ? null : nationId.toString(),
        nationCode);
  }
}
