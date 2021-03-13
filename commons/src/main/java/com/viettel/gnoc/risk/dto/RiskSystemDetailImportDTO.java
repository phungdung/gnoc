package com.viettel.gnoc.risk.dto;

import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.risk.model.RiskSystemDetailEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RiskSystemDetailImportDTO {
  private String id;
  private String systemId;
  private String manageUnitId;
  private String manageUserId;
  private String systemCode;
  private String manageUserName;
  private String actionName;
  private String resultImport;

  public RiskSystemDetailDTO toDTO() {
    return new RiskSystemDetailDTO(
        StringUtils.isNotNullOrEmpty(id) ? Long.valueOf(id) : null,
        StringUtils.isNotNullOrEmpty(systemId) ? Long.valueOf(systemId) : null,
        StringUtils.isNotNullOrEmpty(manageUnitId) ? Long.valueOf(manageUnitId) : null,
        StringUtils.isNotNullOrEmpty(manageUserId) ? Long.valueOf(manageUserId) : null
    );
  }

}
