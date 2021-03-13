/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.od.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.od.model.ConfigPropertyEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author itsol
 * @version 2.0
 * @since 12/03/2018
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfigPropertyDTO extends BaseDto {

  private String key;
  private String value;
  private String description;

  public ConfigPropertyEntity toEntity() {
    return new ConfigPropertyEntity(key, value, description);
  }

}
