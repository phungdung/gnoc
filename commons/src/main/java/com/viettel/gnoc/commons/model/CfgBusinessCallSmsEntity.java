/**
 * @(#)RolesBO.java 8/27/2015 5:34 PM, Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.CfgBusinessCallSmsDTO;
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

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "WFM", name = "CFG_BUSINESS_CALL_SMS")
public class CfgBusinessCallSmsEntity {

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "WFM.cfg_business_call_sms_seq", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  @Column(name = "CFG_TYPE_ID")
  private Long cfgTypeId;

  @Column(name = "CD_ID")
  private Long cdId;

  @Column(name = "CFG_LEVEL")
  private Long cfgLevel;

  public CfgBusinessCallSmsDTO toDTO() {
    CfgBusinessCallSmsDTO dto = new CfgBusinessCallSmsDTO(
        id,
        cfgTypeId,
        cdId,
        cfgLevel
    );
    return dto;
  }
}

