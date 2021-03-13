package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.wo.model.WoEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WoUpdateServiceInfraDTO extends BaseDto {

  //Fields
  private Long woId;
  private String woCode;
  private Long status;
  private String statusName;
  private String woSystem;
  private String accountIsdn;
  private Long serviceId;
  private String serviceName;
  private Long infraType;
  private String infraTypeName;

  public WoEntity toEntity() {
    WoEntity model = new WoEntity(
        woId
        , woCode
        , woSystem
        , status
    );
    return model;
  }
}
