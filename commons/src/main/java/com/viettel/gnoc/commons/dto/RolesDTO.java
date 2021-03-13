
package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.RolesEntity;
import com.viettel.gnoc.commons.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RolesDTO {

  //Fields
  private String roleId;
  private String status;
  private String roleName;
  private String description;
  private String roleCode;

  public RolesEntity toEntity() {
    RolesEntity model = new RolesEntity(
        !StringUtils.validString(roleId) ? null :
            Long.valueOf(roleId),
        !StringUtils.validString(status) ? null :
            Long.valueOf(status),
        roleName,
        description,
        roleCode);
    return model;
  }

}

