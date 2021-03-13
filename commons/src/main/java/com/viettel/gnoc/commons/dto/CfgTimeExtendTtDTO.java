package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.CfgTimeExtendTtEntity;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class CfgTimeExtendTtDTO {

  private String id;
  private String typeId;
  private String alarmGroupId;
  private String reasonId;
  private String priorityId;
  private String timeExtend;
  private String country;

  private String typeName;
  private String alarmGroupName;
  private String reasonName;
  private String priorityName;
  private String countryName;
  private String lastUpdateTime;

  public CfgTimeExtendTtDTO(
      String id, String typeId, String alarmGroupId, String reasonId, String priorityId,
      String timeExtend, String lastUpdateTime, String country) {
    this.id = id;
    this.typeId = typeId;
    this.alarmGroupId = alarmGroupId;
    this.reasonId = reasonId;
    this.priorityId = priorityId;
    this.timeExtend = timeExtend;
    this.lastUpdateTime = lastUpdateTime;
    this.country = country;
  }

  public CfgTimeExtendTtEntity toEntity() {
    CfgTimeExtendTtEntity model = new CfgTimeExtendTtEntity(
        !StringUtils.validString(id) ? null : Long.valueOf(id),
        !StringUtils.validString(typeId) ? null : Long.valueOf(typeId),
        !StringUtils.validString(alarmGroupId) ? null : Long.valueOf(alarmGroupId),
        !StringUtils.validString(reasonId) ? null : Long.valueOf(reasonId),
        !StringUtils.validString(priorityId) ? null : Long.valueOf(priorityId),
        !StringUtils.validString(timeExtend) ? null : Double.valueOf(timeExtend),
        !StringUtils.validString(lastUpdateTime) ? null
            : DateTimeUtils.convertStringToDate(lastUpdateTime),
        country
    );
    return model;
  }

}
