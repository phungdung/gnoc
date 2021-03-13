package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class MrApproveRolesDTO extends BaseDto {

  private String userId;
  private String userName;
  private String unitCode;
  private String unitId;
  private String unitName;
  private String roleCode;
  private String roleName;

}
