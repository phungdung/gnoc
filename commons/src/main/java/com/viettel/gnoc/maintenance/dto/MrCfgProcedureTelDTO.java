package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import com.viettel.gnoc.maintenance.model.MrCfgProcedureTelEntity;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@MultiFieldUnique(message = "{validation.mr.cfgProcedure.isDupplicate}", clazz = MrCfgProcedureTelEntity.class,
    uniqueFields = "marketCode,arrayCode,mrMode,deviceType,networkType", idField = "procedureId")
public class MrCfgProcedureTelDTO extends BaseDto {

  private Long procedureId;
  private String marketCode;
  private String arrayCode;
  private String deviceType;
  private Long deviceTypeCR;
  private String mrContentId;
  private String mrMode;
  private String procedureName;
  private Long cycle;
  private String cycleType;
  private Long genMrBefore;
  private Long mrTime;
  private String genCr;
  private String genWo;
  private Long reGenMrAfter;
  private String genMrCrWoBy;
  private Date expDate;
  private String status;
  private Long arrayAction;
  private Long priorityCr;
  private Long typeCr;
  private Long process;
  private Long dutyType;
  private Long risk;
  private Long cr;
  private Long levelAffect;
  private String descriptionCr;
  private String isServiceAffected;
  private String serviceAffectedId;
  private String woContent;
  private Long cdId;
  private String priorityCode;
  private String impact;
  private String arrayActionName;
  private String attachFileName;
  private String attachFilePath;
  private String mrWorks;
  private Long providerId;
  private Long woType;
  private Long woReceiveUnit;
  private String attachMops;
  private String networkType;
  private String region;
  private Long arrayChild;

  private String arrayCodeName;
  private String marketName;
  private String mrModeName;
  private String cycleName;
  private String statusName;
  private String levelEffect;
  private String isServiceEffect;
  private String serviceEffectId;
  private boolean getNetworkType;
  private boolean getDeviceType;
  private String arrayChildName;
  private String importErorr;

  //tiennv bo sung
  private String mrWorksName;
  private String arrayActorName;
  private String subcategoryName;
  private String dutyTypeName;
  private String serviceEffectName;
  private String genCrName;
  private String typeCrName;
  private String priorityCrName;
  private String deviceTypeCRName;
  private String levelEffectName;
  private String strExpDate;

  //TrungDuong bo sung
  private String genMrBeforeStr;
  private String reGenMrAfterStr;
  private String mrTimeStr;
  private String cycleStr;
  private List<GnocFileDto> lstGnocFiles;

  public MrCfgProcedureTelDTO(Long procedureId) {
    this.procedureId = procedureId;
  }

  public MrCfgProcedureTelDTO(Long procedureId, String marketCode, String arrayCode,
      String deviceType, Long deviceTypeCR, String mrContentId, String mrMode,
      String procedureName, Long cycle, String cycleType, Long genMrBefore, Long mrTime,
      String genCr, String genWo, Long reGenMrAfter, String genMrCrWoBy, Date expDate,
      String status, Long arrayAction, Long priorityCr, Long typeCr, Long process,
      Long dutyType, Long risk, Long cr, Long levelAffect, String descriptionCr,
      String isServiceAffected, String serviceAffectedId, String woContent, Long cdId,
      String priorityCode, String impact, String arrayActionName, String attachFileName,
      String attachFilePath, String mrWorks, Long providerId, Long woType,
      Long woReceiveUnit, String attachMops, String networkType, String region,
      Long arrayChild) {
    this.procedureId = procedureId;
    this.marketCode = marketCode;
    this.arrayCode = arrayCode;
    this.deviceType = deviceType;
    this.deviceTypeCR = deviceTypeCR;
    this.mrContentId = mrContentId;
    this.mrMode = mrMode;
    this.procedureName = procedureName;
    this.cycle = cycle;
    this.cycleType = cycleType;
    this.genMrBefore = genMrBefore;
    this.mrTime = mrTime;
    this.genCr = genCr;
    this.genWo = genWo;
    this.reGenMrAfter = reGenMrAfter;
    this.genMrCrWoBy = genMrCrWoBy;
    this.expDate = expDate;
    this.status = status;
    this.arrayAction = arrayAction;
    this.priorityCr = priorityCr;
    this.typeCr = typeCr;
    this.process = process;
    this.dutyType = dutyType;
    this.risk = risk;
    this.cr = cr;
    this.levelAffect = levelAffect;
    this.descriptionCr = descriptionCr;
    this.isServiceAffected = isServiceAffected;
    this.serviceAffectedId = serviceAffectedId;
    this.woContent = woContent;
    this.cdId = cdId;
    this.priorityCode = priorityCode;
    this.impact = impact;
    this.arrayActionName = arrayActionName;
    this.attachFileName = attachFileName;
    this.attachFilePath = attachFilePath;
    this.mrWorks = mrWorks;
    this.providerId = providerId;
    this.woType = woType;
    this.woReceiveUnit = woReceiveUnit;
    this.attachMops = attachMops;
    this.networkType = networkType;
    this.region = region;
    this.arrayChild = arrayChild;
  }

  public MrCfgProcedureTelEntity toEntity() {
    return new MrCfgProcedureTelEntity(procedureId, marketCode, arrayCode, deviceType, deviceTypeCR,
        mrContentId, mrMode, procedureName, cycle, cycleType, genMrBefore, mrTime, genCr, genWo,
        reGenMrAfter, genMrCrWoBy, expDate, status, arrayAction, priorityCr, typeCr, process,
        dutyType, risk, cr, levelAffect, descriptionCr, isServiceAffected, serviceAffectedId,
        woContent, cdId, priorityCode, impact, arrayActionName, attachFileName, attachFilePath,
        mrWorks, providerId, woType, woReceiveUnit, attachMops, networkType, region, arrayChild);
  }
}
