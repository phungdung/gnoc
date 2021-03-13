/**
 * @(#)AffectedServicesBO.java 8/14/2015 9:50 AM, Copyright 2011 Viettel Telecom. All rights
 * reserved VIETTEL PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.od.model;

import static javax.persistence.GenerationType.SEQUENCE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.viettel.gnoc.od.dto.OdPendingDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @author hungtv77
 * @version 2.0
 * @since 31/12/2020
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "WFM", name = "OD_PENDING")
public class OdPendingEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "OD_PENDING_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "OD_PENDING_ID", unique = true, nullable = false)
  private Long odPendId;

  @Column(name = "INSERT_TIME")
  private Date insertTime;

  @Column(name = "END_PENDING_TIME")
  private Date endPendingTime;

  @Column(name = "REASON_PENDING_ID")
  private String reasonPendingId;

  @Column(name = "REASON_PENDING_NAME")
  private String reasonPendingName;

  @Column(name = "CUSTOMER")
  private String customer;

  @Column(name = "CUS_PHONE")
  private String cusPhone;

  @Column(name = "OD_ID")
  private String odId;

  @Column(name = "OPEN_TIME")
  private Date openTime;

  @Column(name = "OPEN_USER")
  private String openUser;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "PENDING_TYPE")
  private String pendingType;

  public OdPendingDTO toDTO() {
    return new OdPendingDTO(odPendId, insertTime, endPendingTime, reasonPendingId, reasonPendingName, customer,
        cusPhone, odId, openTime, openUser, description, pendingType);
  }

}
