/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import com.viettel.gnoc.maintenance.model.MrCfgProcedureCDEntity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tiennv
 */
@Getter
@Setter
@Slf4j
@MultiFieldUnique(message = "{validation.mr.cfgProcedureCD.isDupplicate}", clazz = MrCfgProcedureCDEntity.class,
    uniqueFields = "marketCode,arrayCode,deviceType,cycle", idField = "procedureId")
public class MrCfgProcedureCDDTO extends BaseDto {

  private String procedureId;

  @NotNull(message = "{validation.mr.cfgProcedure.arrayCode.required}")
  @Size(max = 200, message = "{validation.mr.cfgProcedure.arrayCode.InvalidMaxLength}")
  private String arrayCode;
  private String arrayCodeName;

  @NotNull(message = "{validation.mr.cfgProcedure.marketCode.required}")
  @Size(max = 100, message = "{validation.mr.cfgProcedure.marketCode.InvalidMaxLength}")
  private String marketCode;
  private String marketName;

  @Size(max = 400, message = "{validation.mr.cfgProcedure.deviceType.InvalidMaxLength}")
  private String deviceType;

  @Size(max = 100, message = "{validation.mr.cfgProcedure.mrContentId.InvalidMaxLength}")
  private String mrContentId;

  @Size(max = 375, message = "{validation.mr.cfgProcedure.procedureName.InvalidMaxLength}")
  private String procedureName;

  private Long cycle;
  private String cycleName;
  private Long genMrBefore;
  private Long mrTime;

  @Size(max = 500, message = "{validation.mr.cfgProcedure.arrayActionName.InvalidMaxLength}")
  private String arrayActionName;
  private String woContent;
  private String priorityCode;
  private String impact;

  @Size(max = 750, message = "{validation.mr.cfgProcedure.mrWorks.InvalidMaxLength}")
  private String mrWorks;
  private String defaultSortField;

  public MrCfgProcedureCDDTO() {
    setDefaultSortField("contentId");
  }

  public MrCfgProcedureCDDTO(String procedureId,
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

  public MrCfgProcedureCDEntity toEntity() {
    try {
      MrCfgProcedureCDEntity model = new MrCfgProcedureCDEntity(
          StringUtils.validString(procedureId) ? Long.valueOf(procedureId) : null,
          arrayCode, marketCode, deviceType, mrContentId,
          procedureName,
          StringUtils.validString(cycle) ? Long.valueOf(cycle) : null,
          StringUtils.validString(genMrBefore) ? Long.valueOf(genMrBefore) : null,
          StringUtils.validString(mrTime) ? Long.valueOf(mrTime) : null,
          arrayActionName, woContent, priorityCode, impact, mrWorks
      );
      return model;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

}
