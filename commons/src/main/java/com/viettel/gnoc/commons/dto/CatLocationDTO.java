/**
 * @(#)CatLocationForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.CatLocationEntity;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CatLocationDTO extends BaseDto {

  private String locationId;
  private String locationCode;
  private String locationName;
  private String description;
  private String parentId;
  private String parentCode;
  private String locationAdminLevel;
  private String terrain;
  private String preCodeStation;
  private String status;
  private String place;
  private String featureLocation;
  private String lastUpdateTime;
  private String nationId;
  private String locationIdFull;
  private String locationNameFull;
  private String locationCodeFull;
  private String locationLevel;
  private String parentName;
  private String nationCode;
  private String keyword;

  public CatLocationDTO(
      String locationId, String locationCode, String locationName, String description,
      String parentId, String parentCode, String locationAdminLevel, String terrain,
      String preCodeStation, String status, String place, String featureLocation,
      String lastUpdateTime, String nationId, String nationCode) {
    this.locationId = locationId;
    this.locationCode = locationCode;
    this.locationName = locationName;
    this.description = description;
    this.parentId = parentId;
    this.parentCode = parentCode;
    this.locationAdminLevel = locationAdminLevel;
    this.terrain = terrain;
    this.preCodeStation = preCodeStation;
    this.status = status;
    this.place = place;
    this.featureLocation = featureLocation;
    this.lastUpdateTime = lastUpdateTime;
    this.nationId = nationId;
    this.nationCode = nationCode;
  }

  public CatLocationEntity toEntity() {
    CatLocationEntity model = new CatLocationEntity(
        !StringUtils.validString(locationId) ? null
            : Long.valueOf(locationId),
        locationCode,
        locationName,
        description,
        !StringUtils.validString(parentId) ? null
            : Long.valueOf(parentId),
        parentCode,
        locationAdminLevel,
        terrain,
        preCodeStation,
        !StringUtils.validString(status) ? null
            : Long.valueOf(status),
        !StringUtils.validString(place) ? null
            : Long.valueOf(place),
        !StringUtils.validString(featureLocation) ? null
            : Long.valueOf(featureLocation),
        !StringUtils.validString(lastUpdateTime) ? null
            : DateTimeUtils.convertStringToDate(lastUpdateTime),
        !StringUtils.validString(nationId) ? null
            : Long.valueOf(nationId),
        nationCode);
    return model;
  }
}
