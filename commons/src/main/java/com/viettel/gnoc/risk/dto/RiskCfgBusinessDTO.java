package com.viettel.gnoc.risk.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.risk.model.RiskCfgBusinessEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RiskCfgBusinessDTO extends BaseDto {

  private Long id;
  private Long riskChangeStatusId;
  private Long isRequired;
  private String columnName;
  private Long editable;
  private Long isVisible;
  private String message;

  public RiskCfgBusinessEntity toEntity() {
    return new RiskCfgBusinessEntity(id, riskChangeStatusId, isRequired, columnName, editable,
        isVisible, message);
  }

}
