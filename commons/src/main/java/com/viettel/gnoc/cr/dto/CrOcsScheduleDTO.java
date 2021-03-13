/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.cr.model.CrOscScheduleEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author kienpv
 */

@Setter
@Getter
@NoArgsConstructor
public class CrOcsScheduleDTO {

  private Long crOcsScheduleId;
  private Long userId;
  private String userName;
  private Long crProcessId;
  private String crProcessName;
  private String crProcessParentName;

  public CrOcsScheduleDTO(Long crOcsScheduleId, Long userId,
      Long crProcessId) {
    this.crOcsScheduleId = crOcsScheduleId;
    this.userId = userId;
    this.crProcessId = crProcessId;
  }

  public CrOscScheduleEntity toEntity() {
    return new CrOscScheduleEntity(crOcsScheduleId, userId, crProcessId);
  }
}
