package com.viettel.gnoc.incident.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.incident.model.TroubleCardEntity;
import java.util.Date;
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
public class TroubleCardDTO extends BaseDto {

  private Long troubleId;
  private String cardName;//Ten card
  private String merName;//Ten hang hoa
  private String merCode;//Ma hang hoa
  private String contractCode;//Ma hop dong
  private String serialNo;//Serial number
  private String stationCode;//Ma tram
  private Long troubleCardId;
  private Date updateTime;
  private String crAlternative;//CR thay the
  private String serialAlternative;//Serial thay the
  private String deliveryBill;
  private String slotCardPort;//Slot/card/port
  private String defaultSortField;

  public TroubleCardDTO(Long troubleId, String cardName, String merName, String merCode,
      String contractCode, String serialNo, String stationCode, Long troubleCardId,
      Date updateTime, String crAlternative, String serialAlternative,
      String deliveryBill, String slotCardPort) {
    this.troubleId = troubleId;
    this.cardName = cardName;
    this.merName = merName;
    this.merCode = merCode;
    this.contractCode = contractCode;
    this.serialNo = serialNo;
    this.stationCode = stationCode;
    this.troubleCardId = troubleCardId;
    this.updateTime = updateTime;
    this.crAlternative = crAlternative;
    this.serialAlternative = serialAlternative;
    this.deliveryBill = deliveryBill;
    this.slotCardPort = slotCardPort;
  }

  public TroubleCardEntity toModel() {
    TroubleCardEntity model = new TroubleCardEntity(
        troubleCardId,
        troubleId,
        cardName,
        merName,
        merCode,
        contractCode,
        serialNo,
        stationCode,
        updateTime,
        crAlternative,
        serialAlternative,
        deliveryBill,
        slotCardPort
    );
    return model;

  }
}

