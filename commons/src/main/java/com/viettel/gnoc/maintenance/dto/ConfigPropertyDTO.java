package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.maintenance.model.ConfigPropertyEntity;
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
public class ConfigPropertyDTO extends BaseDto {

  //Fields
  private String key;
  private String value;
  private String description;

  public ConfigPropertyEntity toModel() {
    ConfigPropertyEntity model = new ConfigPropertyEntity(
        key,
        value,
        description);
    return model;
  }
}
