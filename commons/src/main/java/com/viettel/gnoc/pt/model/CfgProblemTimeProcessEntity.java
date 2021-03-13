/**
 * @(#)CfgTimeTroubleProcessBO.java 9/7/2015 9:45 AM, Copyright 2011 Viettel Telecom. All rights
 * reserved VIETTEL PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.pt.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.pt.dto.CfgProblemTimeProcessDTO;
import java.io.Serializable;
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

/**
 * @author tiennv
 * @version 1.0
 * @since 17/04/2018
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(schema = "ONE_TM", name = "CFG_PROBLEM_TIME_PROCESS")
public class CfgProblemTimeProcessEntity implements Serializable {

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "CFG_PROBLEM_TIME_PROCESS_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  @Column(name = "CFG_CODE", unique = true, nullable = false)
  private String cfgCode;

  @Column(name = "TYPE_CODE", nullable = false)
  private String typeCode;

  @Column(name = "PRIORITY_CODE", nullable = false)
  private String priorityCode;

  @Column(name = "RCA_FOUND_TIME", nullable = false)
  private Double rcaFoundTime;

  @Column(name = "WA_FOUND_TIME", nullable = false)
  private Double waFoundTime;

  @Column(name = "SL_FOUND_TIME")
  private Double slFoundTime;

  @Column(name = "LAST_UPDATE_TIME")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date lastUpdateTime;

  public CfgProblemTimeProcessEntity(Long id, String cfgCode, String typeCode, String priorityCode,
      Double rcaFoundTime, Double waFoundTime, Double slFoundTime, Date lastUpdateTime) {
    this.id = id;
    this.cfgCode = cfgCode;
    this.typeCode = typeCode;
    this.priorityCode = priorityCode;
    this.rcaFoundTime = rcaFoundTime;
    this.waFoundTime = waFoundTime;
    this.slFoundTime = slFoundTime;
    this.lastUpdateTime = lastUpdateTime;
  }

  public CfgProblemTimeProcessDTO toDTO() {
    CfgProblemTimeProcessDTO dto = new CfgProblemTimeProcessDTO(
        id == null ? null : String.valueOf(id),
        cfgCode == null ? null : cfgCode,
        typeCode == null ? null : typeCode,
        priorityCode == null ? null : priorityCode,
        rcaFoundTime == null ? null : rcaFoundTime.toString().replaceFirst(".0$", ""),
        waFoundTime == null ? null : waFoundTime.toString().replaceFirst(".0$", ""),
        slFoundTime == null ? null : slFoundTime.toString().replaceFirst(".0$", ""),
        lastUpdateTime
    );
    return dto;
  }
}
