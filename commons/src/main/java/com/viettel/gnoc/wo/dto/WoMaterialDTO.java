package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.model.WoMaterialEntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WoMaterialDTO extends BaseDto {

  //Fields
  private Long materialId;
  private String materialGroupCode;
  private String materialCode;
  private String materialName;
  private String materialGroupName;
  private Date updateTime;
  private Long unitId;
  private String unitName;
  private Long isEnable;
  private String nationCode;
  private Long nationId;

  public WoMaterialEntity toEntity() {
    WoMaterialEntity model = new WoMaterialEntity(
        materialId
        , materialGroupCode
        , materialCode
        , materialName
        , materialGroupName
        , updateTime
        , unitId
        , unitName
        , isEnable
        , nationCode
        , nationId
    );
    return model;
  }
}
