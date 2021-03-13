/**
 * @(#)LanguageExchangeBO.java 11/17/2015 9:59 AM, Copyright 2011 Viettel Telecom. All rights
 * reserved VIETTEL PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
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

/**
 * @author anhmv6
 * @version 1.0
 * @since 11/17/2015 9:59 AM
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "COMMON_GNOC", name = "LANGUAGE_EXCHANGE")
public class LanguageExchangeEntity {

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "COMMON_GNOC.language_exchange_seq", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "LEE_ID", unique = true)
  private Long leeId;
  @Column(name = "APPLIED_SYSTEM")
  private Long appliedSystem;
  @Column(name = "APPLIED_BUSSINESS")
  private Long appliedBussiness;
  @Column(name = "BUSSINESS_ID")
  private Long bussinessId;
  @Column(name = "BUSSINESS_CODE")
  private String bussinessCode;
  @Column(name = "LEE_LOCALE")
  private String leeLocale;
  @Column(name = "LEE_VALUE")
  private String leeValue;

  public LanguageExchangeDTO toDTO() {
    LanguageExchangeDTO dto = new LanguageExchangeDTO(
        leeId,
        appliedSystem,
        appliedBussiness,
        bussinessId,
        bussinessCode,
        leeLocale,
        leeValue
    );
    return dto;
  }
}

