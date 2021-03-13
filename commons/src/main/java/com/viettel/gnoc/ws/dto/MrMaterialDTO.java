package com.viettel.gnoc.ws.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MrMaterialDTO {
  private String materialId;
  private String materialName;
  private String serial;
  private String unitPrice;
  private String dateTime;
  private String deviceType;
  private String marketCode;
  private String leeValue;
  private String materialNameEN;
  private String userManager;
  private String deviceTypeName;
  private String resultImport;
  private String validate;
  private String dateTimeStr;
  private String unitPriceStr;
  private String dateTimeToSearch;
  private String dateTimeFromSearch;
  private String defaultSortField;

  public MrMaterialDTO() {
    setDefaultSortField("name");
  }

  public MrMaterialDTO(String materialId, String materialName, String serial,
      String unitPrice, String dateTime, String deviceType, String marketCode,
      String leeValue, String materialNameEN, String userManager, String deviceTypeName,
      String resultImport, String validate, String dateTimeStr, String unitPriceStr,
      String dateTimeToSearch, String dateTimeFromSearch) {
    this.materialId = materialId;
    this.materialName = materialName;
    this.serial = serial;
    this.unitPrice = unitPrice;
    this.dateTime = dateTime;
    this.deviceType = deviceType;
    this.marketCode = marketCode;
    this.leeValue = leeValue;
    this.materialNameEN = materialNameEN;
    this.userManager = userManager;
    this.deviceTypeName = deviceTypeName;
    this.resultImport = resultImport;
    this.validate = validate;
    this.dateTimeStr = dateTimeStr;
    this.unitPriceStr = unitPriceStr;
    this.dateTimeToSearch = dateTimeToSearch;
    this.dateTimeFromSearch = dateTimeFromSearch;
  }
}
