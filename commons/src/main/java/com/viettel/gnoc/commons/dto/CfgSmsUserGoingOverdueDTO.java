package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.CfgSmsUserGoingOverdueEntity;
import com.viettel.gnoc.commons.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CfgSmsUserGoingOverdueDTO extends BaseDto {

  private String id;
  private String cfgId;
  private String userId;
  private String cfgType;

  public CfgSmsUserGoingOverdueEntity toEntity() {
    CfgSmsUserGoingOverdueEntity model = new CfgSmsUserGoingOverdueEntity(
        !StringUtils.validString(id) ? null : Long.valueOf(id),
        !StringUtils.validString(cfgId) ? null : Long.valueOf(cfgId),
        !StringUtils.validString(userId) ? null : Long.valueOf(userId),
        !StringUtils.validString(cfgType) ? null : Long.valueOf(cfgType)
    );
    return model;
  }
}
