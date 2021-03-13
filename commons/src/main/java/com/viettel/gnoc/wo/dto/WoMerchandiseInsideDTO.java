package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.model.WoMerchandiseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class WoMerchandiseInsideDTO extends BaseDto {

  private Long id;
  private Long woId;
  private String merchandiseCode;
  private String serial;
  private Double quantity;
  private String merchandiseName;
  private Long cause;
  private Long longTermAssetId;
  private String codeStationSource;
  private String codeStationDestination;
  private String downgradeStationCode;
  private String entryCode;
  private String codeStationupgrade;
  private String exportWarehouse;
  private String homeStationCodeUpgrade;
  private String homeStationCodeDowngrade;
  private String resultImport;

  public WoMerchandiseInsideDTO(Long id, Long woId, String merchandiseCode, Double quantity,
      String serial, Long cause) {
    this.id = id;
    this.woId = woId;
    this.merchandiseCode = merchandiseCode;
    this.quantity = quantity;
    this.serial = serial;
    this.cause = cause;
  }

  public WoMerchandiseEntity toEntity() {
    return new WoMerchandiseEntity(id, woId, merchandiseCode, serial, quantity, merchandiseName,
        cause, longTermAssetId);
  }

  public WoMerchandiseDTO toModelOutSide() {
    WoMerchandiseDTO model = new WoMerchandiseDTO(
        StringUtils.validString(id) ? String.valueOf(id) : null,
        StringUtils.validString(woId) ? String.valueOf(woId) : null,
        merchandiseCode,
        serial,
        StringUtils.validString(quantity) ? String.valueOf(quantity) : null,
        merchandiseName,
        StringUtils.validString(cause) ? String.valueOf(cause) : null,
        StringUtils.validString(longTermAssetId) ? String.valueOf(longTermAssetId) : null, null, null, null, null, null, null, null, null, null
    );
    return model;
  }

}
