package com.viettel.gnoc.incident.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.incident.dto.TroubleCardDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(schema = "ONE_TM", name = "TROUBLE_CARD")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class TroubleCardEntity {

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "TROUBLE_CARD_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "TROUBLE_CARD_ID", unique = true, nullable = false)
  private Long troubleCardId;
  @Column(name = "TROUBLE_ID")
  private Long troubleId;
  @Column(name = "CARD_NAME")
  private String cardName;
  @Column(name = "MER_NAME")
  private String merName;
  @Column(name = "MER_CODE")
  private String merCode;
  @Column(name = "CONTRACT_CODE")
  private String contractCode;
  @Column(name = "SERIAL_NO")
  private String serialNo;
  @Column(name = "STATION_CODE")
  private String stationCode;
  @Column(name = "UPDATE_TIME")
  private Date updateTime;
  @Column(name = "CR_ALTERNATIVE")
  private String crAlternative;
  @Column(name = "SERIAL_ALTERNATIVE")
  private String serialAlternative;
  @Column(name = "DELIVERY_BILL")
  private String deliveryBill;
  @Column(name = "SLOT_CARD_PORT")
  private String slotCardPort;

  public TroubleCardDTO toDTO() {
    TroubleCardDTO dto = new TroubleCardDTO(
        troubleId,
        cardName,
        merName,
        merCode,
        contractCode,
        serialNo,
        stationCode,
        troubleCardId,
        updateTime
        ,
        crAlternative,
        serialAlternative,
        deliveryBill,
        slotCardPort
    );
    return dto;
  }
}
