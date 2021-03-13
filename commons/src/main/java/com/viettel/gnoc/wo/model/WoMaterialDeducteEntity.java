package com.viettel.gnoc.wo.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.dto.WoMaterialDeducteInsideDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "WFM", name = "WO_MATERIAL_DEDUCTE")
public class WoMaterialDeducteEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "WO_MATERIAL_DEDUCTE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "WO_MATERIAL_DEDUCTE_ID", unique = true, nullable = false)
  private Long woMaterialDeducteId;

  @Column(name = "WO_ID")
  private Long woId;

  @Column(name = "USER_ID")
  private Long userId;

  @Column(name = "USER_NAME")
  private String userName;

  @Column(name = "MATERIAL_ID")
  private Long materialId;

  @Column(name = "IS_DEDUCTE")
  private Long isDeducte;

  @Column(name = "SEND_IM_RESULT")
  private String sendImResult;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CREATE_DATE")
  private Date createDate;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "SEND_IM_TIME")
  private Date sendImTime;

  @Column(name = "ACTION_ID")
  private Long actionId;

  @Column(name = "QUANTITY")
  private Double quantity;

  @Column(name = "REASON_ID")
  private Long reasonId;

  @Column(name = "FIRST_METERS_INDEX")
  private Double firstMetersIndex;

  @Column(name = "LAST_METERS_INDEX")
  private Double lastMetersIndex;

  @Column(name = "CABLE_LENGTH_ON_NIMS")
  private Double cableLengthOnNims;

  @Column(name = "SERIAL")
  private String serial;

  @Column(name = "TYPE")
  private Long type;  // 0 - Vat tu thay the; 1 - hang hoa thay the ; 2 - hang hoa thu hoi

  @Column(name = "NATION_CODE")
  private String nationCode;

  @Column(name = "NATION_ID")
  private Long nationId;

  public WoMaterialDeducteInsideDTO toDTO() {
    return new WoMaterialDeducteInsideDTO(woMaterialDeducteId, woId, userId, userName, materialId,
        isDeducte, sendImResult, createDate, sendImTime, actionId, quantity, reasonId,
        firstMetersIndex, lastMetersIndex, cableLengthOnNims,
        serial,
        type,
        nationCode,
        nationId);
  }

}
