package com.viettel.gnoc.risk.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.risk.model.RiskFileEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RiskFileDTO extends BaseDto {

  private Long riskFileId;
  private Long riskId;
  private String fileName;
  private String path;

  public RiskFileEntity toEntity() {
    return new RiskFileEntity(riskFileId, riskId, fileName, path);
  }

}
