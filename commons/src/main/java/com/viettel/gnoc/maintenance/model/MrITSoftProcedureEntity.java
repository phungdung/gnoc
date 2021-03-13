package com.viettel.gnoc.maintenance.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.maintenance.dto.MrITSoftProcedureDTO;
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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "MR_CFG_PROCEDURE_IT_SOFT")
public class MrITSoftProcedureEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "MR_CFG_PROCEDURE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "PROCEDURE_ID", nullable = false)
  private Long procedureId;

  @Column(name = "ARRAY_CODE")
  private String arrayCode;

  @Column(name = "MARKET_CODE")
  private String marketCode;

  @Column(name = "REGION")
  private String region;

  @Column(name = "DEVICE_TYPE")
  private String deviceType;

  @Column(name = "DEVICE_TYPE_CR")
  private String deviceTypeCR;

  @Column(name = "IMPORTANT_LEVEL")
  private String importantLevel;

  @Column(name = "MR_CONTENT_ID")
  private String mrContentId;

  @Column(name = "MR_MODE")
  private String mrMode;

  @Column(name = "MR_TYPE")
  private String mrType;

  @Column(name = "CR_TYPE")
  private String crType;

  @Column(name = "PROCEDURE_NAME")
  private String procedureName;

  @Column(name = "CYCLE")
  private Long cycle;

  @Column(name = "CYCLE_TYPE")
  private String cycleType;

  @Column(name = "GEN_MR_BEFORE")
  private Long genMrBefore;

  @Column(name = "MR_TIME")
  private Long mrTime;

  @Column(name = "GEN_CR")
  private String genCr;

  @Column(name = "GEN_WO")
  private String genWo;

  @Column(name = "RE_GEN_MR_AFTER")
  private Long reGenMrAfter;

  @Column(name = "GEN_MR_CR_WO_BY")
  private String genMrCrWoBy;

  @Column(name = "EXP_DATE")
  private Date expDate;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "ARRAY_ACTION")
  private Long arrayAction;

  @Column(name = "ARRAY_ACTION_NAME")
  private String arrayActionName;

  @Column(name = "PRIORITY_CR")
  private Long priorityCr;

  @Column(name = "TYPE_CR")
  private Long typeCr;

  @Column(name = "PROCESS")
  private Long process;

  @Column(name = "DUTY_TYPE")
  private Long dutyType;

  @Column(name = "RISK")
  private Long risk;

  @Column(name = "CR")
  private Long cr;

  @Column(name = "DESCRIPTION_CR")
  private String descriptionCr;

  @Column(name = "LEVEL_AFFECT")
  private Long levelEffect;

  @Column(name = "IS_SERVICE_AFFECTED")
  private String isServiceEffect;

  @Column(name = "SERVICE_AFFECTED_ID")
  private String serviceEffectId;

  @Column(name = "WO_CONTENT")
  private String woContent;

  @Column(name = "CD_ID")
  private Long cdId;

  @Column(name = "PRIORITY_CODE")
  private String priorityCode;

  @Column(name = "IMPACT")
  private String impact;

  @Column(name = "MR_WORKS")
  private String mrWorks;

  @Column(name = "ATTACH_FILE_NAME")
  private String attachFileName;

  @Column(name = "ATTACH_FILE_PATH")
  private String attachFilePath;

  @Column(name = "CR_WORKS")
  private String crWorks;

  @Column(name = "SYSTEM_AUTO")
  private Long systemAuto;

  @Column(name = "PATH_MOP")
  private String pathMop;


  public MrITSoftProcedureDTO toDTO() {
    MrITSoftProcedureDTO dto = new MrITSoftProcedureDTO(
        procedureId,
        arrayCode,
        marketCode,
        region,
        deviceType,
        deviceTypeCR,
        importantLevel,
        mrContentId,
        mrMode,
        mrType,
        crType,
        procedureName,
        cycle,
        cycleType,
        genMrBefore,
        mrTime,
        genCr,
        genWo,
        reGenMrAfter,
        genMrCrWoBy,
        expDate,
        status,
        arrayAction,
        arrayActionName,
        priorityCr,
        typeCr,
        process,
        dutyType,
        risk,
        cr,
        descriptionCr,
        levelEffect,
        isServiceEffect, serviceEffectId, woContent,
        cdId,
        priorityCode, impact, mrWorks, attachFileName, attachFilePath, crWorks,
        systemAuto, pathMop
    );
    return dto; //To change body of generated methods, choose Tools | Templates.
  }


}
