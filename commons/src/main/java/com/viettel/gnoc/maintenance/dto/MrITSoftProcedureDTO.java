package com.viettel.gnoc.maintenance.dto;


import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import com.viettel.gnoc.maintenance.model.MrITSoftProcedureEntity;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@Slf4j
@NoArgsConstructor
@MultiFieldUnique(message = "{validation.mrITSoftProcedureDTO.multiple.unique}", clazz = MrITSoftProcedureEntity.class, uniqueFields = "marketCode,arrayCode,deviceType,importantLevel", idField = "procedureId")
public class MrITSoftProcedureDTO extends BaseDto {

  private Long procedureId;
  @NotEmpty(message = "{validation.mrITSoftProcedureDTO.null.arrayCode}")
  private String arrayCode;
  private String arrayCodeName;
  @NotEmpty(message = "{validation.mrITSoftProcedureDTO.null.marketCode}")
  private String marketCode;
  private String marketName;
  @NotEmpty(message = "{validation.mrITSoftProcedureDTO.null.deviceType}")
  private String deviceType;
  private String deviceTypeCR;
  private String deviceTypeCRName;
  @NotEmpty(message = "{validation.mrITSoftProcedureDTO.null.importantLevel}")
  private String importantLevel;
  private String importantLevelName;
  @NotEmpty(message = "{validation.mrITSoftProcedureDTO.null.mrContentId}")
  private String mrContentId;
  @NotEmpty(message = "{validation.mrITSoftProcedureDTO.null.mrMode}")
  private String mrMode;
  private String mrModeName;
  @NotEmpty(message = "{validation.mrITSoftProcedureDTO.null.mrType}")
  private String mrType;
  private String crType;
  @NotEmpty(message = "{validation.mrITSoftProcedureDTO.null.procedureName}")
  private String procedureName;
  @NotNull(message = "{validation.mrITSoftProcedureDTO.null.cycle}")
  private Long cycle;
  private String cycleName;
  @NotEmpty(message = "{validation.mrITSoftProcedureDTO.null.cycleType}")
  private String cycleType;
  @NotNull(message = "{validation.mrITSoftProcedureDTO.null.genMrBefore}")
  private Long genMrBefore;
  private Long mrTime;
  private String genCr;
  private String genWo;
  private Long reGenMrAfter;
  @NotEmpty(message = "{validation.mrITSoftProcedureDTO.null.genMrCrWoBy}")
  private String genMrCrWoBy;
  private Date expDate;
  @NotEmpty(message = "{validation.mrITSoftProcedureDTO.null.status}")
  private String status;
  private String statusName;
  private Long arrayAction;
  private String arrayActionName;
  private Long priorityCr;
  private Long typeCr;
  private Long process;
  //    @NotNull(message = "{validation.mrITSoftProcedureDTO.null.dutyType}")
  private Long dutyType;
  private String dutyTypeName;
  private Long risk;
  private Long cr;
  private String crName;
  private String descriptionCr;
  private Long levelEffect;
  private String levelEffectName;
  private String isServiceEffect;
  private String serviceEffectId;
  private String serviceEffectName;
  private String woContent;
  private Long cdId;
  private String priorityCode;
  private String impact;
  @NotEmpty(message = "{validation.mrITSoftProcedureDTO.null.mrWorks}")
  private String mrWorks;
  private String region;
  private String mrId;
  private String crId;
  private String attachFileName;
  private String attachFilePath;
  private String crWorks;
  private Long systemAuto;
  private String pathMop;

  private List<GnocFileDto> lstGnocFiles;

  //export
  private String cycleTypeName;
  private String typeCrName;
  private String priorityCrName;
  private String genCrName;
  private String genWoName;

  public MrITSoftProcedureDTO(
      Long procedureId,
      String arrayCode,
      String marketCode,
      String region,
      String deviceType,
      String deviceTypeCR,
      String importantLevel,
      String mrContentId,
      String mrMode,
      String mrType,
      String crType,
      String procedureName,
      Long cycle,
      String cycleType,
      Long genMrBefore,
      Long mrTime,
      String genCr,
      String genWo,
      Long reGenMrAfter,
      String genMrCrWoBy,
      Date expDate,
      String status,
      Long arrayAction,
      String arrayActionName,
      Long priorityCr,
      Long typeCr,
      Long process,
      Long dutyType,
      Long risk,
      Long cr,
      String descriptionCr,
      Long levelEffect,
      String isServiceEffect,
      String serviceEffectId,
      String woContent,
      Long cdId,
      String priorityCode,
      String impact,
      String mrWorks,
      String attachFileName,
      String attachFilePath,
      String crWorks,
      Long systemAuto,
      String pathMop
  ) {
    this.procedureId = procedureId;
    this.arrayCode = arrayCode;
    this.marketCode = marketCode;
    this.region = region;
    this.deviceType = deviceType;
    this.deviceTypeCR = deviceTypeCR;
    this.importantLevel = importantLevel;
    this.mrContentId = mrContentId;
    this.mrMode = mrMode;
    this.mrType = mrType;
    this.crType = crType;
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
    this.arrayActionName = arrayActionName;
    this.priorityCr = priorityCr;
    this.typeCr = typeCr;
    this.process = process;
    this.dutyType = dutyType;
    this.risk = risk;
    this.cr = cr;
    this.descriptionCr = descriptionCr;
    this.levelEffect = levelEffect;
    this.isServiceEffect = isServiceEffect;
    this.serviceEffectId = serviceEffectId;
    this.woContent = woContent;
    this.cdId = cdId;
    this.priorityCode = priorityCode;
    this.impact = impact;
    this.mrWorks = mrWorks;
    this.attachFileName = attachFileName;
    this.attachFilePath = attachFilePath;
    this.crWorks = crWorks;
    this.systemAuto = systemAuto;
    this.pathMop = pathMop;
  }

  public MrITSoftProcedureEntity toModel() {
    try {
      MrITSoftProcedureEntity model = new MrITSoftProcedureEntity(
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
          priorityCode, impact, mrWorks, attachFileName, attachFilePath, crWorks, systemAuto,
          pathMop
      );
      return model;
    } catch (Exception e) {
      return null;
    }
  }

}
