package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.model.MrImpactedNodesEntity;
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
public class MrImpactedNodesDTO extends BaseDto {

  private String minsId;
  private String mrId;
  private String ipId;
  private String deviceId;
  private String insertTime;
  private String deviceName;
  private String deviceCode;
  private String deviceCodeOld;
  private String ip;

  public MrImpactedNodesDTO(String minsId, String mrId, String ipId, String deviceId,
      String insertTime) {
    this.minsId = minsId;
    this.mrId = mrId;
    this.ipId = ipId;
    this.deviceId = deviceId;
    this.insertTime = insertTime;
  }

  public MrImpactedNodesEntity toEntity() {
    try {
      MrImpactedNodesEntity model = new MrImpactedNodesEntity(
          StringUtils.validString(minsId)
              ? Long.valueOf(minsId) : null,
          StringUtils.validString(mrId)
              ? Long.valueOf(mrId) : null,
          StringUtils.validString(ipId)
              ? Long.valueOf(ipId) : null,
          StringUtils.validString(deviceId)
              ? Long.valueOf(deviceId) : null,
          StringUtils.validString(insertTime)
              ? DateTimeUtils.convertStringToDate(insertTime) : null);
      return model;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }
}
