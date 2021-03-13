package com.viettel.gnoc.incident.dto;

import com.viettel.gnoc.incident.model.TTChangeStatusRoleEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class TTChangeStatusRoleDTO {

  private Long id;
  private Long ttChangeStatusId;
  private Long roleId;
  private String roleName;

  public TTChangeStatusRoleDTO(Long id, Long ttChangeStatusId, Long roleId) {
    this.id = id;
    this.ttChangeStatusId = ttChangeStatusId;
    this.roleId = roleId;
  }

  public TTChangeStatusRoleEntity toEntity() {
    return new TTChangeStatusRoleEntity(id, ttChangeStatusId, roleId);
  }
}
