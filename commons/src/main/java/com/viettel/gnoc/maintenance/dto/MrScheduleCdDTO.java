/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.maintenance.model.MrScheduleCdEntity;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tiennv
 */
@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class MrScheduleCdDTO extends BaseDto {

  private Long scheduleId;
  private String marketCode;
  private String marketName;
  private String deviceType;
  private Long deviceCdId;
  private String deviceName;
  private Long procedureId;
  private Date lastDate;
  private Date nextDate;
  private Date nextDateModify;
  private String station;
  private String stationCode;
  private Date updatedDate;
  private Long mrId;
  private String modifyUser;
  private Date modifyDate;

  private String procedureName;
  private String cycle;
  private String deviceTypeName;

  private Date dateModifyFromSearch;
  private Date dateModifyToSearch;
  private String userMrHard;
  private String woCode;

  public MrScheduleCdDTO(
      String marketCode,
      Long scheduleId,
      String deviceType,
      Long deviceCdId,
      String deviceName,
      Long procedureId,
      Date lastDate,
      Date nextDate,
      Date nextDateModify,
      String station,
      Date updatedDate,
      Long mrId,
      String modifyUser,
      Date modifyDate,
      String cycle
  ) {
    this.marketCode = marketCode;
    this.scheduleId = scheduleId;
    this.deviceType = deviceType;
    this.deviceCdId = deviceCdId;
    this.deviceName = deviceName;
    this.procedureId = procedureId;
    this.lastDate = lastDate;
    this.nextDate = nextDate;
    this.nextDateModify = nextDateModify;
    this.station = station;
    this.updatedDate = updatedDate;
    this.mrId = mrId;
    this.modifyUser = modifyUser;
    this.modifyDate = modifyDate;
    this.cycle = cycle;
  }

  public MrScheduleCdEntity toEntity() {
    try {
      MrScheduleCdEntity model = new MrScheduleCdEntity(
          scheduleId,
          marketCode,
          deviceType,
          deviceCdId,
          deviceName,
          procedureId,
          lastDate,
          nextDate,
          nextDateModify,
          station,
          updatedDate,
          mrId,
          modifyUser,
          modifyDate,
          cycle == null ? null : Long.valueOf(cycle)
      );
      return model;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

}
