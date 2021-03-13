package com.viettel.gnoc.sr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.sr.model.SRRoleUserEntity;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
public class SRRoleUserInSideDTO extends BaseDto {

  //Fields
  private Long roleUserId;
  @NotEmpty(message = "{validation.srRoleUser.null.roleCode}")
  private String roleCode;
  private String username;
  private String status;
  private String createdUser;
  private Date createdTime;
  private String updatedUser;
  private Date updatedTime;
  @NotEmpty(message = "{validation.srRoleUser.null.country}")
  private String country;
  private String countryName;

  @NotNull(message = "{validation.srRoleUser.null.unitId}")
  private Long unitId;

  private String unitName;
  private Long isLeader;

  private String usernameStr;
  private String isLeaderStr;
  private String unitIdStr;
  private String statusName;
  private String roleName;
  private String groupRole;
  private String parentCode;
  private String resultImport;
  private String roleCodeStr;
  private String action;
  private String actionName;
  private String autoCreateSR;
  private List<Long> lstUnitId;
  private String defaultSortField = "name";
  private boolean insertSR;
  private String message;
  private String key;
  private String userId;

  public SRRoleUserInSideDTO(Long roleUserId, String roleCode, String username, String status,
      String createdUser, Date createdTime, String updatedUser, Date updatedTime,
      String country, Long unitId, Long isLeader) {
    this.roleUserId = roleUserId;
    this.roleCode = roleCode;
    this.username = username;
    this.status = status;
    this.createdUser = createdUser;
    this.createdTime = createdTime;
    this.updatedUser = updatedUser;
    this.updatedTime = updatedTime;
    this.country = country;
    this.unitId = unitId;
    this.isLeader = isLeader;
  }

  public SRRoleUserInSideDTO(String username) {
    this.username = username;
  }

  public SRRoleUserEntity toEntity() {
    SRRoleUserEntity model = new SRRoleUserEntity(
        roleUserId
        , roleCode
        , username
        , status
        , createdUser
        , createdTime
        , updatedUser
        , updatedTime
        , country
        , unitId
        , isLeader
    );
    return model;
  }

  public SRRoleUserInSideDTO(String message, String key) {
    this.message = message;
    this.key = key;
  }
}
