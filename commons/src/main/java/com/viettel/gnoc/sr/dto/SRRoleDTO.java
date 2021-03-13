package com.viettel.gnoc.sr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import com.viettel.gnoc.sr.model.SRRoleEntity;
import java.util.Date;
import javax.validation.constraints.NotEmpty;
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
@MultiFieldUnique(message = "{validation.srRole.null.unique}", clazz = SRRoleEntity.class, uniqueFields = "roleCode,country", idField = "roleId")
public class SRRoleDTO extends BaseDto {

  //Fields
  private Long roleId;
  @NotEmpty(message = "{validation.srRole.null.roleCode}")
  private String roleCode;
  @NotEmpty(message = "{validation.srRole.null.roleName}")
  private String roleName;
  private String createdUser;
  private Date createdTime;
  private String updatedUser;
  private Date updatedTime;
  @NotEmpty(message = "{validation.srRole.null.country}")
  private String country;
  private String countryName;
  private String groupRole;
  private String parentCode;

  private String action;
  private String actionName;
  private String groupRoleName;
  private String parentName;
  private String resultImport;
  private String defaultSortField = "name";

  public SRRoleDTO(Long roleId, String roleCode, String roleName, String createdUser,
      Date createdTime, String updatedUser, Date updatedTime, String country,
      String groupRole, String parentCode) {
    this.roleId = roleId;
    this.roleCode = roleCode;
    this.roleName = roleName;
    this.createdUser = createdUser;
    this.createdTime = createdTime;
    this.updatedUser = updatedUser;
    this.updatedTime = updatedTime;
    this.country = country;
    this.groupRole = groupRole;
    this.parentCode = parentCode;
  }

  public SRRoleEntity toEntity() {
    SRRoleEntity model = new SRRoleEntity(
        roleId
        , roleCode
        , roleName
        , createdUser
        , createdTime
        , updatedUser
        , updatedTime
        , country
        , groupRole
        , parentCode
    );
    return model;
  }
}
