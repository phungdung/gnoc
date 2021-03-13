package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.wo.model.WoKTTSInfoEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WoKTTSInfoDTO extends BaseDto {

  private Long id;
  private Long woId;
  private Long contractId;
  private String contractCode;
  private String processActionName;
  private String contractPartner;
  private Long processActionId;
  private Long activeEnvironmentId;

  public WoKTTSInfoEntity toEntity() {
    return new WoKTTSInfoEntity(id, woId, contractId, contractCode, processActionName,
        contractPartner, processActionId, activeEnvironmentId);
  }
}
