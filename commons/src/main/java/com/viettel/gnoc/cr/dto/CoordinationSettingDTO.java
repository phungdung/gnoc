/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import com.viettel.gnoc.cr.model.CoordinationSettingEntity;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@MultiFieldUnique(message = "{validation.CoordinationSetting.null.unique}", clazz = CoordinationSettingEntity.class, uniqueFields = "unitId,groupId", idField = "id")
public class CoordinationSettingDTO extends BaseDto {

  private Long id;
  private Long unitId;
  private Long groupId;
  @NotEmpty(message = "{validation.CoordinationSetting.null.unitCode}")
  private String unitCode;
  @NotEmpty(message = "{validation.CoordinationSetting.null.groupCode}")
  private String groupCode;
  private String unitName;
  private String groupName;

  public CoordinationSettingDTO(Long id, Long unitId, Long groupId) {
    this.id = id;
    this.unitId = unitId;
    this.groupId = groupId;
  }

  public CoordinationSettingEntity toEntity() {
    CoordinationSettingEntity model = new CoordinationSettingEntity(
        id
        , unitId
        , groupId
    );
    return model;
  }
}
