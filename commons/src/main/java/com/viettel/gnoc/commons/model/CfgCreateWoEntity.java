/**
 * @(#)CfgTimeTroubleProcessBO.java 9/7/2015 9:45 AM, Copyright 2011 Viettel Telecom. All rights
 * reserved VIETTEL PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.CfgCreateWoDTO;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
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


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(schema = "COMMON_GNOC", name = "CFG_CREATE_WO")
public class CfgCreateWoEntity {

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "CFG_CREATE_WO_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  @Column(name = "TYPE_ID", nullable = false)
  private Long typeId;

  @Column(name = "ALARM_GROUP_ID", nullable = false)
  private Long alarmGroupId;

  @Column(name = "LAST_UPDATE_TIME")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date lastUpdateTime;

  public CfgCreateWoEntity(Long id, Long typeId, Long alarmGroupId, Date lastUpdateTime) {
    this.id = id;
    this.typeId = typeId;
    this.alarmGroupId = alarmGroupId;
    this.lastUpdateTime = lastUpdateTime;

  }

  public CfgCreateWoDTO toDTO() {
    CfgCreateWoDTO dto = new CfgCreateWoDTO(
        id == null ? null : id.toString(),
        typeId == null ? null : typeId.toString(),
        alarmGroupId == null ? null : alarmGroupId.toString(),
        lastUpdateTime == null ? null : DateTimeUtils
            .convertDateToString(lastUpdateTime, "dd/MM/yyyy HH:mm:ss")
    );
    return dto;
  }
}
