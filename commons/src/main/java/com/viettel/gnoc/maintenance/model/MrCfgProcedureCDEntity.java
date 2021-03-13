/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.maintenance.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.maintenance.dto.MrCfgProcedureCDDTO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author hungnt
 */
@Entity
@Table(schema = "OPEN_PM", name = "MR_CFG_PROCEDURE_CD")
@Getter
@Setter
@NoArgsConstructor
public class MrCfgProcedureCDEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "MR_CFG_PROCEDURE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "PROCEDURE_ID", unique = true, nullable = false)
  private Long procedureId;

  @Column(name = "ARRAY_CODE")
  private String arrayCode;

  @Column(name = "MARKET_CODE")
  private String marketCode;

  @Column(name = "DEVICE_TYPE")
  private String deviceType;

  @Column(name = "MR_CONTENT_ID")
  private String mrContentId;

  @Column(name = "PROCEDURE_NAME", length = 250)
  private String procedureName;

  @Column(name = "CYCLE")
  private Long cycle;

  @Column(name = "GEN_MR_BEFORE")
  private Long genMrBefore;

  @Column(name = "MR_TIME")
  private Long mrTime;

  @Column(name = "ARRAY_ACTION_NAME", length = 500)
  private String arrayActionName;

  @Column(name = "WO_CONTENT", length = 250)
  private String woContent;

  @Column(name = "PRIORITY_CODE")
  private String priorityCode;

  @Column(name = "IMPACT")
  private String impact;

  @Column(name = "MR_WORKS")
  private String mrWorks;

  public MrCfgProcedureCDEntity(Long procedureId,
      String arrayCode,
      String marketCode,
      String deviceType,
      String mrContentId,
      String procedureName,
      Long cycle,
      Long genMrBefore,
      Long mrTime,
      String arrayActionName,
      String woContent,
      String priorityCode,
      String impact,
      String mrWorks
  ) {
    this.procedureId = procedureId;
    this.arrayCode = arrayCode;
    this.marketCode = marketCode;
    this.deviceType = deviceType;
    this.mrContentId = mrContentId;
    this.procedureName = procedureName;
    this.cycle = cycle;
    this.genMrBefore = genMrBefore;
    this.mrTime = mrTime;
    this.arrayActionName = arrayActionName;
    this.woContent = woContent;
    this.priorityCode = priorityCode;
    this.impact = impact;
    this.mrWorks = mrWorks;
  }

  public MrCfgProcedureCDDTO toDTO() {
    MrCfgProcedureCDDTO dto = new MrCfgProcedureCDDTO(
        procedureId == null ? null : procedureId.toString()
        , arrayCode == null ? null : arrayCode
        , marketCode == null ? null : marketCode
        , deviceType == null ? null : deviceType
        , mrContentId == null ? null : mrContentId
        , procedureName == null ? null : procedureName
        , cycle == null ? null : cycle
        , genMrBefore == null ? null : genMrBefore
        , mrTime == null ? null : mrTime
        , arrayActionName == null ? null : arrayActionName
        , woContent == null ? null : woContent
        , priorityCode == null ? null : priorityCode
        , impact == null ? null : impact
        , mrWorks == null ? null : mrWorks
    );
    return dto; //To change body of generated methods, choose Tools | Templates.
  }


}
