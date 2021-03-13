/**
 * @(#)RoleUserForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */

package com.viettel.gnoc.commons.dto;


import com.viettel.gnoc.commons.model.CfgBusinessCallSmsUserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CfgBusinessCallSmsUserDTO extends BaseDto {

  //Fields
  private Long id;
  private Long cfgBusinessId;
  private Long userId;
  private String fullName;

  public CfgBusinessCallSmsUserDTO(Long id, Long cfgBusinessId, Long userId) {
    this.id = id;
    this.cfgBusinessId = cfgBusinessId;
    this.userId = userId;
  }

  public CfgBusinessCallSmsUserEntity toEntity() {
    CfgBusinessCallSmsUserEntity entity = new CfgBusinessCallSmsUserEntity(
        id, cfgBusinessId, userId);
    return entity;
  }
}

