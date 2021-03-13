package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.model.WoTestServiceMapEntity;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class WoTestServiceMapDTO extends BaseDto {

  private String id;
  private String woId;
  private String woConfigId;
  private String insertTime;
  private String cdId;
  private String woSubId;
  private String fileId;

  private String defaultSortField;

  public WoTestServiceMapDTO(String id, String woId, String woConfigId, String insertTime,
      String cdId, String woSubId, String fileId) {
    this.id = id;
    this.woId = woId;
    this.woConfigId = woConfigId;
    this.insertTime = insertTime;
    this.cdId = cdId;
    this.woSubId = woSubId;
    this.fileId = fileId;
  }

  public WoTestServiceMapEntity toEntity() {
    try {
      WoTestServiceMapEntity model = new WoTestServiceMapEntity(
          !StringUtils.validString(id) ? null : Long.valueOf(id),
          !StringUtils.validString(woId) ? null : Long.valueOf(woId),
          !StringUtils.validString(woConfigId) ? null : Long.valueOf(woConfigId),
          !StringUtils.validString(insertTime) ? null : DateTimeUtils.convertStringToDate(insertTime),
          !StringUtils.validString(cdId) ? null : Long.valueOf(cdId),
          woSubId,
          !StringUtils.validString(fileId) ? null : Long.valueOf(fileId)
      );
      return model;
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      return null;
    }
  }
}
