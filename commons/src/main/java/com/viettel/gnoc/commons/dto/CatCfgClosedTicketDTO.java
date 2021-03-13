package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.CatCfgClosedTicketEntity;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import javax.validation.constraints.NotEmpty;
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
//@MultiFieldUnique(message = "{validation.cfg.null.unique}", clazz = CatCfgClosedTicketEntity.class, uniqueFields = "woTypeId,typeId,alarmGroupId", idField = "id")
public class CatCfgClosedTicketDTO extends BaseDto {

  //Fields
  private String id;
  @NotEmpty(message = "{validation.WoTypeId.null}")
  private String woTypeId;
  @NotEmpty(message = "{validation.TypeId.null}")
  private String typeId;
  @NotEmpty(message = "{validation.AlarmGroupId.null}")
  private String alarmGroupId;
  private String lastUpdateTime;

  private String woTypeName;
  private String alarmGroupName;
  private String typeName;
  private String woTypeCode;
  private String alarmGroupCode;
  private String typeCode;
  private String defaultSortField;
  //Constructor

  public CatCfgClosedTicketDTO(String id, String woTypeId, String typeId, String alarmGroupId,
      String lastUpdateTime) {
    this.id = id;
    this.woTypeId = woTypeId;
    this.typeId = typeId;
    this.alarmGroupId = alarmGroupId;
    this.lastUpdateTime = lastUpdateTime;
  }

  public CatCfgClosedTicketEntity toEntity() {
    CatCfgClosedTicketEntity model = new CatCfgClosedTicketEntity(
        !StringUtils.validString(id) ? null : Long.valueOf(id),
        !StringUtils.validString(woTypeId) ? null : Long.valueOf(woTypeId),
        !StringUtils.validString(typeId) ? null : Long.valueOf(typeId),
        !StringUtils.validString(alarmGroupId) ? null : Long.valueOf(alarmGroupId),
        !StringUtils.validString(lastUpdateTime) ? null
            : DateTimeUtils.convertStringToDate(lastUpdateTime)
    );
    return model;
  }

}
