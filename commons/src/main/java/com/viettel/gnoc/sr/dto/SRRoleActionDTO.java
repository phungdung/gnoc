package com.viettel.gnoc.sr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.sr.model.SRRoleActionsEntity;
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
//@MultiFieldUnique(message = "{validation.srRole.null.unique}", clazz = SRRoleEntity.class, uniqueFields = "roleCode", idField = "roleId")
public class SRRoleActionDTO extends BaseDto {

  //Fields
  private Long roleActionId;
  private String roleType;
  //  @NotEmpty(message = "{validation.srRole.null.roleCode}")
  private String roleCode;
  private String currentStatus;
  private String nextStatus;

  private int statusRoleAction;

  private String actions;
  private String actionsName;
  private String countryName;
  //  @NotEmpty(message = "{validation.srRole.null.country}")
  private String country;
  private Date createdTime;
  private String createdUser;
  private Date updatedTime;
  private String updatedUser;

  private String currentStatusName;
  private String nextStatusName;

  private String serviceCode;
  private String serviceName;
  private Long configId;
  private String configName;

  private String flowName;
  private Long flowId;
  //  @NotEmpty(message = "{validation.srRole.null.roleName}")
  private String roleName;
  private String groupRole;
  private String groupRoleName;
  private String roleTypeName;

  public SRRoleActionDTO(Long roleActionId, String roleType, String roleCode
      , String currentStatus, String nextStatus
      , String actions, String createdUser
      , Date createdTime, String updatedUser
      , Date updatedTime, String country
      , String serviceCode, Long flowId
      , String groupRole) {
    this.roleActionId = roleActionId;
    this.roleType = roleType;
    this.roleCode = roleCode;
    this.currentStatus = currentStatus;
    this.nextStatus = nextStatus;
    this.actions = actions;
    this.createdUser = createdUser;
    this.createdTime = createdTime;
    this.updatedUser = updatedUser;
    this.updatedTime = updatedTime;
    this.country = country;
    this.serviceCode = serviceCode;
    this.flowId = flowId;
    this.groupRole = groupRole;
  }

  public SRRoleActionsEntity toEntity() {
    SRRoleActionsEntity model = new SRRoleActionsEntity(
        roleActionId
        , roleType
        , roleCode
        , currentStatus
        , nextStatus
        , actions
        , createdUser
        , createdTime
        , updatedUser
        , updatedTime
        , country
        , serviceCode
        , flowId
        , groupRole
    );
    return model;
  }
}
