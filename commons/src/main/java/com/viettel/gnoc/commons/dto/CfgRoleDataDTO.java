
package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.CategoryEntity;
import com.viettel.gnoc.commons.model.CfgRoleDataEntity;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author truongnt
 * @version 1.0
 * @since 19/08/2020 9:00 AM
 */
@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MultiFieldUnique(message = "{validation.cfgRoleData.null.unique}", clazz = CategoryEntity.class, uniqueFields = "username,system", idField = "id")
public class CfgRoleDataDTO extends BaseDto {

  private Long id;
  private String username;
  private Long system;
  private String unitId;
  private Long role;
  private Long status;
  private String statusName;
  private String unitName;
  private String roleName;
  private String updatedUser;
  private String systemName;
  private Date updatedTime;
  private String resultImport;
  private String auditUnitId;
  private String locationId;
  private Long type;
  private String typeName;

  public CfgRoleDataDTO(Long id, String username, Long system, String unitId,
      Long role, Long status, String updateUser, Date updateTime, String auditUnitId, String locationId, Long type) {
    this.id = id;
    this.username = username;
    this.unitId = unitId;
    this.system = system;
    this.role = role;
    this.status = status;
    this.updatedUser = updateUser;
    this.updatedTime = updateTime;
    this.auditUnitId = auditUnitId;
    this.locationId = locationId;
    this.type = type;
  }

  public CfgRoleDataEntity toEntity() {
    CfgRoleDataEntity entity = new CfgRoleDataEntity(id, username, system,
        unitId, role, status, updatedUser, updatedTime, auditUnitId, locationId, type);
    return entity;
  }
}
