package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.CfgInfoTtSpmEntity;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
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
@MultiFieldUnique(message = "{validation.commons.cfgInfoTtSpm.isDupplicate.TT}", clazz = CfgInfoTtSpmEntity.class, uniqueFields = "typeId,alarmGroupId,subCategoryId", idField = "id")
public class CfgInfoTtSpmDTO extends BaseDto {

  //Fields
  private String defaultSortField;
  private String id;
  private String typeId;
  private String alarmGroupId;
  private String subCategoryId;
  private String troubleName;

  private String typeName;
  private String alarmGroupName;
  private String subCategoryName;

  private String p_system;
  private String p_bussiness;
  private String p_leeLocale;

  public CfgInfoTtSpmDTO(String id, String typeId, String alarmGroupId, String subCategoryId,
      String troubleName) {
    this.id = id;
    this.typeId = typeId;
    this.alarmGroupId = alarmGroupId;
    this.subCategoryId = subCategoryId;
    this.troubleName = troubleName;
  }

  public CfgInfoTtSpmEntity toEntity() {
    return new CfgInfoTtSpmEntity(
        StringUtils.isStringNullOrEmpty(this.id) ? null : Long.parseLong(this.id),
        StringUtils.isStringNullOrEmpty(this.typeId) ? null : Long.parseLong(this.typeId),
        StringUtils.isStringNullOrEmpty(this.alarmGroupId) ? null
            : Long.parseLong(this.alarmGroupId),
        StringUtils.isStringNullOrEmpty(this.subCategoryId) ? null
            : Long.parseLong(this.subCategoryId),
        this.troubleName
    );
  }
}
