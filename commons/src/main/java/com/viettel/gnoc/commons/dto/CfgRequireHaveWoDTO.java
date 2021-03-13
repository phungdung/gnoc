package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.CfgRequireHaveWoEntity;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
@MultiFieldUnique(message = "{validation.cfgRequireWo.null.unique}", clazz = CfgRequireHaveWoEntity.class, uniqueFields = "typeId,alarmGroupId,reasonId", idField = "id")
public class CfgRequireHaveWoDTO extends BaseDto {

  //Fields
  private String defaultSortField;
  private Long id;
  @NotNull(message = "{validation.TypeId.null}")
  private Long typeId;
  @NotNull(message = "{validation.AlarmGroupId.null}")
  private Long alarmGroupId;
  private Long reasonId;
  private String woTypeId;
  private String typeName;
  private String alarmGroupName;
  private String reasonName;
  private String lastUpdateTime;
  private String woTypeName;

  private Boolean isRoot;

  public CfgRequireHaveWoDTO(Long id, Long typeId, Long alarmGroupId, Long reasonId,
      String woTypeId, String lastUpdateTime) {
    this.id = id;
    this.typeId = typeId;
    this.alarmGroupId = alarmGroupId;
    this.reasonId = reasonId;
    this.woTypeId = woTypeId;
    this.lastUpdateTime = lastUpdateTime;
  }

  public CfgRequireHaveWoEntity toEntity() {
    CfgRequireHaveWoEntity model = new CfgRequireHaveWoEntity(
        id, typeId, alarmGroupId, reasonId, woTypeId,
        !StringUtils.validString(lastUpdateTime) ? null
            : DateTimeUtils.convertStringToDate(lastUpdateTime)
    );
    return model;
  }
}
