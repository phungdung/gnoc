package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.CfgCreateWoEntity;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
@MultiFieldUnique(message = "{validation.CfgCreateWoInsiteDTO.unique}", clazz = CfgCreateWoEntity.class, uniqueFields = "typeId,alarmGroupId", idField = "id")
public class CfgCreateWoInsiteDTO extends BaseDto {

  private Long id;
  @NotNull(message = "{validation.CfgCreateWoInsiteDTO.null.typeId}")
  private Long typeId;
  @NotNull(message = "{validation.CfgCreateWoInsiteDTO.null.alarmGroupId}")
  private Long alarmGroupId;
  private Date lastUpdateTime;

  private String alarmGroupName;
  private String typeName;
  private String defaultSortField;

  public CfgCreateWoInsiteDTO(Long id, Long typeId, Long alarmGroupId, Date lastUpdateTime) {
    this.id = id;
    this.typeId = typeId;
    this.alarmGroupId = alarmGroupId;
    this.lastUpdateTime = lastUpdateTime;
  }

  public CfgCreateWoEntity toEntity() {
    return new CfgCreateWoEntity(id, typeId, alarmGroupId, lastUpdateTime);
  }
}
