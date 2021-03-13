package com.viettel.gnoc.risk.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.risk.model.RiskSystemFileEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RiskSystemFileDTO extends BaseDto {

  private Long riskSystemFileId;
  private Long systemId;
  private String fileName;
  private String path;

  public RiskSystemFileEntity toEntity() {
    return new RiskSystemFileEntity(riskSystemFileId, systemId, fileName, path);
  }

}
