package com.viettel.gnoc.od.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.od.model.OdChangeStatusRoleEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class OdChangeStatusRoleDTO extends BaseDto {

  //Fields
  private Long id;
  private Long odChangeStatusId;
  private Long roleId;

  public OdChangeStatusRoleDTO() {

  }

  public OdChangeStatusRoleDTO(Long id, Long odChangeStatusId, Long roleId) {
    this.id = id;
    this.odChangeStatusId = odChangeStatusId;
    this.roleId = roleId;
  }

  public OdChangeStatusRoleEntity toEntity() {
    return new OdChangeStatusRoleEntity(this.id, this.odChangeStatusId, this.roleId);
  }
}
