package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.CatServiceEntity;
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
public class CatServiceDTO extends BaseDto {

  //Fields
  private Long serviceId;
  private String serviceCode;
  private String serviceName;
  private String serviceCodeCc1;
  private String serviceCodeCc2;
  private Long isCheckQosInternet;
  private Long isCheckQosTh;
  private Long isCheckQrCode;
  private Long isEnable;

  public CatServiceEntity toEntity() {
    CatServiceEntity model = new CatServiceEntity(
        serviceId
        , serviceCode
        , serviceName
        , serviceCodeCc1
        , serviceCodeCc2
        , isCheckQosInternet
        , isCheckQosTh
        , isCheckQrCode
        , isEnable
    );
    return model;
  }

}
