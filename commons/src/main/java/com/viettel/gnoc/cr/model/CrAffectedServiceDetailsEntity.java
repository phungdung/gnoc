/**
 * @(#)CrAffectedServiceDetailsBO.java 9/4/2015 5:32 PM, Copyright 2011 Viettel Telecom. All rights
 * reserved VIETTEL PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.cr.dto.CrAffectedServiceDetailsDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "CR_AFFECTED_SERVICE_DETAILS")
public class CrAffectedServiceDetailsEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "CASDS_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "CASDS_ID", unique = true, nullable = false)
  private Long casdsId;

  @Column(name = "CR_ID")
  private Long crId;

  @Column(name = "AFFECTED_SERVICE_ID")
  private Long affectedServiceId;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "INSERT_TIME")
  private Date insertTime;

  public CrAffectedServiceDetailsEntity(
      Long casdsId, Long crId, Long affectedServiceId, Date insertTime) {
    this.casdsId = casdsId;
    this.crId = crId;
    this.affectedServiceId = affectedServiceId;
    this.insertTime = insertTime;
  }

  public CrAffectedServiceDetailsDTO toDTO() {
    CrAffectedServiceDetailsDTO dto = new CrAffectedServiceDetailsDTO(
        casdsId == null ? null : casdsId.toString(),
        crId == null ? null : crId.toString(),
        affectedServiceId == null ? null : affectedServiceId.toString(),
        insertTime == null ? null
            : DateTimeUtils.convertDateToString(insertTime, Constants.ddMMyyyy)
    );
    return dto;
  }
}

