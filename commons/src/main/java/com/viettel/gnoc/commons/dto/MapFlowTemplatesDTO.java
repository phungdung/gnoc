package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.MapFlowTemplatesEntity;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@MultiFieldUnique(message = "{validation.mapFlowTemplates.isDupplicate.TT}", clazz = MapFlowTemplatesEntity.class,
    uniqueFields = "typeId,alarmGroupId", idField = "id")
public class MapFlowTemplatesDTO extends BaseDto {

  private String id;
  private String typeId;
  private String alarmGroupId;
  private String typeName;
  private String alarmGroupName;
  private String lastUpdateTime;
  private String updateUser;
  private String userID;
  private String searchEndTime;
  private String searchStartTime;


  public MapFlowTemplatesDTO(String id, String typeId, String alarmGroupId, String lastUpdateTime,
      String userID) {
    this.id = id;
    this.typeId = typeId;
    this.alarmGroupId = alarmGroupId;
    this.lastUpdateTime = lastUpdateTime;
    this.userID = userID;
  }

  public MapFlowTemplatesEntity toEntity() {
    MapFlowTemplatesEntity entity = new MapFlowTemplatesEntity(
        StringUtils.validString(id) ? Long.valueOf(id)
            : null,
        StringUtils.validString(typeId) ? Long.valueOf(typeId)
            : null,
        StringUtils.validString(alarmGroupId) ? Long.valueOf(alarmGroupId)
            : null,
        StringUtils.validString(lastUpdateTime) ? DateTimeUtils
            .convertStringToDate(lastUpdateTime)
            : null,
        StringUtils.validString(userID) ? Long.valueOf(userID)
            : null
    );
    return entity;
  }

}
