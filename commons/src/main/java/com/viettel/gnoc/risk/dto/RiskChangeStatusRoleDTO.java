package com.viettel.gnoc.risk.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.risk.model.RiskChangeStatusRoleEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RiskChangeStatusRoleDTO extends BaseDto {

  private Long id;
  private Long riskChangeStatusId;
  private Long roleId;

  private String roleName;

  public RiskChangeStatusRoleDTO(Long id, Long riskChangeStatusId, Long roleId) {
    this.id = id;
    this.riskChangeStatusId = riskChangeStatusId;
    this.roleId = roleId;
  }

  public RiskChangeStatusRoleEntity toEntity() {
    return new RiskChangeStatusRoleEntity(id, riskChangeStatusId, roleId);
  }

}
