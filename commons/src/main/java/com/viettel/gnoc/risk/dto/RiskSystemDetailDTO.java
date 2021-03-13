package com.viettel.gnoc.risk.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.risk.model.RiskSystemDetailEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RiskSystemDetailDTO extends BaseDto {

  private Long id;
  private Long systemId;
  private Long manageUnitId;
  private Long manageUserId;

  private String manageUserName;
  private String manageUserFullName;
  private String manageUnitName;
  private String manageUnitCode;

  public RiskSystemDetailDTO(Long id, Long systemId, Long manageUnitId, Long manageUserId) {
    this.id = id;
    this.systemId = systemId;
    this.manageUnitId = manageUnitId;
    this.manageUserId = manageUserId;
  }

  public RiskSystemDetailEntity toEntity() {
    return new RiskSystemDetailEntity(id, systemId, manageUnitId, manageUserId);
  }

}
