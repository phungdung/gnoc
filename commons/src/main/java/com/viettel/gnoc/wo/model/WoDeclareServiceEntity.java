package com.viettel.gnoc.wo.model;

import com.viettel.gnoc.wo.dto.WoDeclareServiceDTO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "WO_DECLARE_SERVICE")
public class WoDeclareServiceEntity {

  @Id
  @Column(name = "WO_ID", nullable = false)
  private Long woId;

  @Column(name = "subscriber_Name")
  private String subscriberName;

  @Column(name = "account")
  private String account;

  @Column(name = "account_Uplink")
  private String accountUplink;

  @Column(name = "service")
  private String service;

  @Column(name = "effect_Type")
  private String effectType;

  @Column(name = "technical_Clues")
  private String technicalClues;

  public WoDeclareServiceDTO toDTO() {
    return new WoDeclareServiceDTO(woId, subscriberName, account, accountUplink, service,
        effectType, technicalClues);
  }


}
