/**
 * @(#)RoleUserForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */

package com.viettel.gnoc.commons.dto;


import com.viettel.gnoc.commons.model.RoleUserEntity;
import com.viettel.gnoc.commons.utils.StringUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author ITSOL
 */

@Getter
@Setter
@NoArgsConstructor
public class RoleUserDTO {

  //Fields
  private String roleId;
  private String isAdmin;
  private String userId;
  private String isActive;
  private String roleUserId;

  public RoleUserDTO(
      String roleUserId, String isAdmin, String userId, String isActive, String roleId) {
    this.roleId = roleId;
    this.isAdmin = isAdmin;
    this.userId = userId;
    this.isActive = isActive;
    this.roleUserId = roleUserId;
  }

  public RoleUserEntity toEntity() {
    RoleUserEntity entity = new RoleUserEntity(
        !StringUtils.validString(roleUserId) ? null :
            Long.valueOf(roleUserId),
        !StringUtils.validString(isAdmin) ? null :
            Long.valueOf(isAdmin),
        !StringUtils.validString(userId) ? null :
            Long.valueOf(userId),
        !StringUtils.validString(isActive) ? null :
            Long.valueOf(isActive),
        !StringUtils.validString(roleId) ? null :
            Long.valueOf(roleId));
    return entity;
  }
}

