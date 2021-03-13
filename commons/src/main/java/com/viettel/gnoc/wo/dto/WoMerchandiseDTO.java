package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ITSOL
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j

public class WoMerchandiseDTO {

  private String id;
  private String woId;
  private String merchandiseCode;
  private String serial;
  private String quantity;
  private String merchandiseName;
  private String cause;
  private String longTermAssetId;
  private String codeStationSource;
  private String codeStationDestination;
  private String downgradeStationCode;
  private String entryCode;
  private String codeStationupgrade;
  private String exportWarehouse;
  private String homeStationCodeUpgrade;
  private String homeStationCodeDowngrade;
  private String resultImport;

  public WoMerchandiseInsideDTO toModelInSide() {
    WoMerchandiseInsideDTO model = new WoMerchandiseInsideDTO(
        StringUtils.validString(id) ? Long.valueOf(id) : null,
        StringUtils.validString(woId) ? Long.valueOf(woId) : null,
        merchandiseCode,
        serial,
        StringUtils.validString(quantity) ? Double.valueOf(quantity) : null,
        merchandiseName,
        StringUtils.validString(cause) ? Long.valueOf(cause) : null,
        StringUtils.validString(longTermAssetId) ? Long.valueOf(longTermAssetId) : null, null, null, null, null, null, null, null, null, null
    );
    return model;
  }

}
