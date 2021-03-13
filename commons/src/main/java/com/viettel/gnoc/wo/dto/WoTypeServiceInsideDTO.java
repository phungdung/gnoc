package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.model.WoTypeServiceEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WoTypeServiceInsideDTO extends BaseDto {

  private Long woTypeServiceId;
  private Long woTypeId;
  private Long serviceId;
  private Long isCheckQosInternet;
  private Long isCheckQosTh;
  private Long isCheckQrCode;
  private String woTypeIdName;

  public WoTypeServiceInsideDTO(Long woTypeServiceId, Long woTypeId, Long serviceId,
      Long isCheckQosInternet, Long isCheckQosTh, Long isCheckQrCode) {
    this.woTypeServiceId = woTypeServiceId;
    this.woTypeId = woTypeId;
    this.serviceId = serviceId;
    this.isCheckQosInternet = isCheckQosInternet;
    this.isCheckQosTh = isCheckQosTh;
    this.isCheckQrCode = isCheckQrCode;
  }

  public WoTypeServiceEntity toEntity() {
    return new WoTypeServiceEntity(woTypeServiceId, woTypeId, serviceId, isCheckQosInternet,
        isCheckQosTh, isCheckQrCode);
  }

  public WoTypeServiceDTO toWoTypeServiceDTO() {
    WoTypeServiceDTO model = new WoTypeServiceDTO(
        StringUtils.validString(woTypeServiceId) ? String.valueOf(woTypeServiceId) : null,
        StringUtils.validString(woTypeId) ? String.valueOf(woTypeId) : null,
        StringUtils.validString(serviceId) ? String.valueOf(serviceId) : null,
        StringUtils.validString(isCheckQosInternet) ? String.valueOf(isCheckQosInternet) : null,
        StringUtils.validString(isCheckQosTh) ? String.valueOf(isCheckQosTh) : null,
        StringUtils.validString(isCheckQrCode) ? String.valueOf(isCheckQrCode) : null,
        woTypeIdName
    );
    return model;
  }
}
