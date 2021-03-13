package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@Slf4j
public class WoTypeServiceDTO {

  private String woTypeServiceId;
  private String woTypeId;
  private String serviceId;
  private String isCheckQosInternet;
  private String isCheckQosTh;
  private String isCheckQrCode;
  private String woTypeIdName;

  private String defaultSortField;

  public WoTypeServiceDTO() {
    setDefaultSortField("woTypeServiceId");
  }

  public WoTypeServiceDTO(String woTypeServiceId, String woTypeId, String woTypeIdName,
      String serviceId, String isCheckQosInternet, String isCheckQosTh,
      String isCheckQrCode) {
    this.woTypeServiceId = woTypeServiceId;
    this.woTypeId = woTypeId;
    this.woTypeIdName = woTypeIdName;
    this.serviceId = serviceId;
    this.isCheckQosInternet = isCheckQosInternet;
    this.isCheckQosTh = isCheckQosTh;
    this.isCheckQrCode = isCheckQrCode;
  }

  public WoTypeServiceInsideDTO toInsideDTO() {
    WoTypeServiceInsideDTO model = new WoTypeServiceInsideDTO(
        StringUtils.validString(woTypeServiceId) ? Long.valueOf(woTypeServiceId) : null,
        StringUtils.validString(woTypeId) ? Long.valueOf(woTypeId) : null,
        StringUtils.validString(serviceId) ? Long.valueOf(serviceId) : null,
        StringUtils.validString(isCheckQosInternet) ? Long.valueOf(isCheckQosInternet) : null,
        StringUtils.validString(isCheckQosTh) ? Long.valueOf(isCheckQosTh) : null,
        StringUtils.validString(isCheckQrCode) ? Long.valueOf(isCheckQrCode) : null,
        woTypeIdName);
    return model;
  }
}
