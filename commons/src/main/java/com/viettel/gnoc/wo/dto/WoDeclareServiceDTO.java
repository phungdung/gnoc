package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.wo.model.WoDeclareServiceEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WoDeclareServiceDTO extends BaseDto {

  private Long woId;
  private String subscriberName;
  private String account;
  private String accountUplink;
  private String service;
  private String effectType;
  private String technicalClues;

  public WoDeclareServiceEntity toEntity() {
    return new WoDeclareServiceEntity(woId, subscriberName, account, accountUplink, service,
        effectType, technicalClues);
  }


}
