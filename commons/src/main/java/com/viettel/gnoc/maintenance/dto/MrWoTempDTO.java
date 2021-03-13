package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.model.MrWoTempEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class MrWoTempDTO {

  //Fields
  private String woId;
  private String woWfmId;
  private String parentId;
  private String woCode;
  private String woContent;
  private String createDate;
  private String woSystem;
  private String woSystemId;
  private String woTypeId;
  private String createPersonId;
  private String cdId;
  private String ftId;
  private String status;
  private String priorityId;
  private String startTime;
  private String endTime;
  private String finishTime;
  private String result;
  private String stationId;
  private String stationCode;
  private String lastUpdateTime;
  private String fileName;
  private String woDescription;

  private String defaultSortField;

  public MrWoTempDTO(String woId, String woWfmId, String parentId, String woCode, String woContent,
      String createDate, String woSystem, String woSystemId, String woTypeId, String createPersonId,
      String cdId, String ftId, String status, String priorityId, String startTime, String endTime,
      String finishTime, String result, String stationId, String stationCode, String lastUpdateTime,
      String fileName, String woDescription) {
    this.woId = woId;
    this.woWfmId = woWfmId;
    this.parentId = parentId;
    this.woCode = woCode;
    this.woContent = woContent;
    this.createDate = createDate;
    this.woSystem = woSystem;
    this.woSystemId = woSystemId;
    this.woTypeId = woTypeId;
    this.createPersonId = createPersonId;
    this.cdId = cdId;
    this.ftId = ftId;
    this.status = status;
    this.priorityId = priorityId;
    this.startTime = startTime;
    this.endTime = endTime;
    this.finishTime = finishTime;
    this.result = result;
    this.stationId = stationId;
    this.stationCode = stationCode;
    this.lastUpdateTime = lastUpdateTime;
    this.fileName = fileName;
    this.woDescription = woDescription;
  }

  public MrWoTempEntity toEntity() {
    try {
      MrWoTempEntity model = new MrWoTempEntity(
          StringUtils.validString(woId)
              ? Long.valueOf(woId) : null,
          StringUtils.validString(woWfmId)
              ? Long.valueOf(woWfmId) : null,
          StringUtils.validString(parentId)
              ? Long.valueOf(parentId) : null,
          woCode,
          woContent,
          StringUtils.validString(createDate)
              ? DateTimeUtils.convertStringToDate(createDate) : null,
          woSystem,
          woSystemId,
          StringUtils.validString(woTypeId)
              ? Long.valueOf(woTypeId) : null,
          StringUtils.validString(createPersonId)
              ? Long.valueOf(createPersonId) : null,
          StringUtils.validString(cdId)
              ? Long.valueOf(cdId) : null,
          StringUtils.validString(ftId)
              ? Long.valueOf(ftId) : null,
          StringUtils.validString(status)
              ? Long.valueOf(status) : null,
          StringUtils.validString(priorityId)
              ? Long.valueOf(priorityId) : null,
          StringUtils.validString(startTime)
              ? DateTimeUtils.convertStringToDate(startTime) : null,
          StringUtils.validString(endTime)
              ? DateTimeUtils.convertStringToDate(endTime) : null,
          StringUtils.validString(finishTime)
              ? DateTimeUtils.convertStringToDate(finishTime) : null,
          StringUtils.validString(result)
              ? Long.valueOf(result) : null,
          StringUtils.validString(stationId)
              ? Long.valueOf(stationId) : null,
          stationCode,
          StringUtils.validString(lastUpdateTime)
              ? DateTimeUtils.convertStringToDate(lastUpdateTime) : null,
          fileName,
          woDescription);
      return model;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }
}
