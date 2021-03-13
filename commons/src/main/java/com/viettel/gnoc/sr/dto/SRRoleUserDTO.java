package com.viettel.gnoc.sr.dto;

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
public class SRRoleUserDTO {

  private String roleUserId;
  private String roleCode;
  private String userName;
  private String status;
  private String createdUser;
  private String createdTime;
  private String updatedUser;
  private String updatedTime;
  private String country;
  private String unitId;
  private String roleName;
  private String groupRole;
  private String parentCode;
  private String unitName;
  private String isLeader;
  private String userId;
  private String message;
  private String key;
  private String serviceCode;
  private String defaultSortField = "name";

  public SRRoleUserDTO(String roleUserId, String roleCode, String userName, String status,
      String createdUser, String createdTime, String updatedUser, String updatedTime,
      String country, String unitId, String isLeader) {
    this.roleUserId = roleUserId;
    this.roleCode = roleCode;
    this.userName = userName;
    this.status = status;
    this.createdUser = createdUser;
    this.createdTime = createdTime;
    this.updatedUser = updatedUser;
    this.updatedTime = updatedTime;
    this.country = country;
    this.unitId = unitId;
    this.isLeader = isLeader;
  }

  public SRRoleUserDTO(String message, String key) {
    this.message = message;
    this.key = key;
  }
}
