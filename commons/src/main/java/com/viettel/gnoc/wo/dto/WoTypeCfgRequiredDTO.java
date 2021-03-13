package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.wo.model.WoTypeCfgRequiredEntity;
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
public class WoTypeCfgRequiredDTO extends BaseDto {

  //Fields
  private Long id;
  private Long woTypeId;
  private Long cfgId;
  private Long value;
  private String cfgName;
  private String cfgCode;

  public WoTypeCfgRequiredDTO(Long id, Long woTypeId, Long cfgId, Long value) {
    this.id = id;
    this.woTypeId = woTypeId;
    this.cfgId = cfgId;
    this.value = value;
  }

  public WoTypeCfgRequiredEntity toEntity() {
    WoTypeCfgRequiredEntity model = new WoTypeCfgRequiredEntity(
        id
        , woTypeId
        , cfgId
        , value
    );
    return model;
  }
}
