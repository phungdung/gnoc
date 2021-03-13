package com.viettel.gnoc.maintenance.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.maintenance.dto.MrCfgProcedureITHardDTO;
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
@Table(schema = "OPEN_PM", name = "MR_CFG_PROCEDURE_IT_HARD")
public class MrCfgProcedureITHardEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "MR_CFG_PROCEDURE_IT_HARD_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "PROCEDURE_ID", length = 22, unique = true, nullable = false)
  private Long procedureId;
  @Column(name = "MARKET_CODE", length = 400)
  private String marketCode;
  @Column(name = "ARRAY_CODE", length = 800)
  private String arrayCode;
  @Column(name = "DEVICE_TYPE", length = 400)
  private String deviceType;
  @Column(name = "DEVICE_TYPE_CR", length = 22)
  private Long deviceTypeCR;
  @Column(name = "MR_CONTENT_ID", length = 400)
  private String mrContentId;
  @Column(name = "MR_MODE")
  private String mrMode;
  @Column(name = "PROCEDURE_NAME", length = 500)
  private String procedureName;
  @Column(name = "CYCLE", length = 22)
  private Long cycle;
  @Column(name = "CYCLE_TYPE", length = 4)
  private String cycleType;
  @Column(name = "GEN_MR_BEFORE", length = 22)
  private Long genMrBefore;
  @Column(name = "MR_TIME", length = 22)
  private Long mrTime;
  @Column(name = "GEN_CR", length = 20)
  private String genCr;
  @Column(name = "GEN_WO", length = 20)
  private String genWo;
  @Column(name = "RE_GEN_MR_AFTER", length = 22)
  private Long reGenMrAfter;
  @Column(name = "GEN_MR_CR_WO_BY", length = 4)
  private String genMrCrWoBy;
  @Column(name = "EXP_DATE", length = 7)
  private Date expDate;
  @Column(name = "STATUS", length = 20)
  private String status;
  @Column(name = "ARRAY_ACTION", length = 22)
  private Long arrayAction;
  @Column(name = "PRIORITY_CR", length = 22)
  private Long priorityCr;
  @Column(name = "TYPE_CR", length = 22)
  private Long typeCr;
  @Column(name = "PROCESS", length = 22)
  private Long process;
  @Column(name = "DUTY_TYPE", length = 22)
  private Long dutyType;
  @Column(name = "RISK", length = 22)
  private Long risk;
  @Column(name = "CR", length = 22)
  private Long cr;
  @Column(name = "LEVEL_AFFECT", length = 22)
  private Long levelAffect;
  @Column(name = "DESCRIPTION_CR", length = 1000)
  private String descriptionCr;
  @Column(name = "IS_SERVICE_AFFECTED", length = 4)
  private String isServiceAffected;
  @Column(name = "SERVICE_AFFECTED_ID", length = 1000)
  private String serviceAffectedId;
  @Column(name = "WO_CONTENT", length = 500)
  private String woContent;
  @Column(name = "CD_ID", length = 22)
  private Long cdId;
  @Column(name = "PRIORITY_CODE", length = 1000)
  private String priorityCode;
  @Column(name = "IMPACT", length = 1000)
  private String impact;
  @Column(name = "ARRAY_ACTION_NAME", length = 2000)
  private String arrayActionName;
  @Column(name = "ATTACH_FILE_NAME", length = 255)
  private String attachFileName;
  @Column(name = "ATTACH_FILE_PATH", length = 255)
  private String attachFilePath;
  @Column(name = "MR_WORKS", length = 3000)
  private String mrWorks;
  @Column(name = "PROVIDER_ID", length = 22)
  private Long providerId;
  @Column(name = "WO_TYPE", length = 22)
  private Long woType;
  @Column(name = "WO_RECEIVE_UNIT", length = 22)
  private Long woReceiveUnit;
  @Column(name = "ATTACH_MOPS", length = 1000)
  private String attachMops;
  @Column(name = "REGION", length = 200)
  private String region;
  @Column(name = "ARRAY_CHILD")
  private Long arrayChild;

  public MrCfgProcedureITHardDTO toDTO() {
    return new MrCfgProcedureITHardDTO(procedureId, marketCode, arrayCode, deviceType,
        deviceTypeCR, mrContentId, mrMode, procedureName, cycle, cycleType, genMrBefore, mrTime,
        genCr, genWo, reGenMrAfter, genMrCrWoBy, expDate, status, arrayAction, priorityCr, typeCr,
        process, dutyType, risk, cr, levelAffect, descriptionCr, isServiceAffected,
        serviceAffectedId, woContent, cdId, priorityCode, impact, arrayActionName, attachFileName,
        attachFilePath, mrWorks, providerId, woType, woReceiveUnit, attachMops, region, arrayChild);
  }
}
