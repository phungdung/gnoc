/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.model.MrCrRecreatedEntity;
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
public class MrCrRecreatedDTO extends BaseDto {

  private String recreatedId;
  private String mrId;
  private String crId;

  public MrCrRecreatedEntity toEntity() {
    try {
      MrCrRecreatedEntity model = new MrCrRecreatedEntity(
          StringUtils.validString(recreatedId) ? Long.valueOf(recreatedId) : null,
          StringUtils.validString(mrId) ? Long.valueOf(mrId) : null,
          StringUtils.validString(crId) ? Long.valueOf(crId) : null);
      return model;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }
}
