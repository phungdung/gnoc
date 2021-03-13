/**
 * @(#)CfgTimeTroubleProcessForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.pt.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import com.viettel.gnoc.commons.validator.SizeByte;
import com.viettel.gnoc.commons.validator.Unique;
import com.viettel.gnoc.pt.model.CfgProblemTimeProcessEntity;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ITSOL
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@MultiFieldUnique(message = "{validation.problem.cfgProblem.isDupplicate.PT}", clazz = CfgProblemTimeProcessEntity.class,
    uniqueFields = "typeCode,priorityCode", idField = "id")
@Unique(message = "{validation.problem.cfgProblem.isDupplicate.CfgCode}", clazz = CfgProblemTimeProcessEntity.class,
    uniqueField = "cfgCode", idField = "id")
public class CfgProblemTimeProcessDTO extends BaseDto {

  //Fields
  private String id;
  @NotNull(message = "{validation.problem.cfgProblem.cfgCodeRequired}")
  @SizeByte(max = 500, message = "{validation.problem.cfgProblem.cfgCodeInvalidMaxLength}")
  private String cfgCode;

  @NotNull(message = "{validation.problem.cfgProblem.cfgTypeCodeRequired}")
  private String typeCode;

  private String typeName;

  @NotNull(message = "{validation.problem.cfgProblem.cfgPriorityCodeRequired}")
  private String priorityCode;

  private String priorityName;

  @NotNull(message = "{validation.problem.cfgProblem.cfgRcaFoundTimeRequired}")
  @Size(max = 10, message = "{validation.problem.cfgProblem.cfgRcaFoundTimeInvalidMaxLength}")
  @Pattern(regexp = "^([0-9]*([.][0-9]+){0,1})$", message = "{validation.problem.cfgProblem.cfgRcaFoundTimeInvalidNumber}")
  private String rcaFoundTime;

  @NotNull(message = "{validation.problem.cfgProblem.cfgWaFoundTimeRequired}")
  @Size(max = 10, message = "{validation.problem.cfgProblem.cfgWaFoundTimeInvalidMaxLength}")
  @Pattern(regexp = "^([0-9]*([.][0-9]+){0,1})$", message = "{validation.problem.cfgProblem.cfgWaFoundTimeInvalidNumber}")
  private String waFoundTime;

  @NotNull(message = "{validation.problem.cfgProblem.cfgSlFoundTimeRequired}")
  @Size(max = 10, message = "{validation.problem.cfgProblem.cfgSlFoundTimeInvalidMaxLength}")
  @Pattern(regexp = "^([0-9]*([.][0-9]+){0,1})$", message = "{validation.problem.cfgProblem.cfgSlFoundTimeInvalidNumber}")
  private String slFoundTime;

  private Date lastUpdateTime;

  private String lastUpdateTimeFrom;

  private String lastUpdateTimeTo;

  private Long offset;

  private List<String> lstTypeId;

  private List<String> lstPriorityId;

  private List<CfgProblemTimeProcessDTO> cfgProblemTimeProcessDTOS;

  private Date createDatePT;

  private Long locationId;

  public CfgProblemTimeProcessDTO(String id, String cfgCode, String typeCode, String priorityCode,
      String rcaFoundTime, String waFoundTime, String slFoundTime, Date lastUpdateTime) {
    this.id = id;
    this.cfgCode = cfgCode;
    this.typeCode = typeCode;
    this.priorityCode = priorityCode;
    this.rcaFoundTime = rcaFoundTime;
    this.waFoundTime = waFoundTime;
    this.slFoundTime = slFoundTime;
    this.lastUpdateTime = lastUpdateTime;
  }
  //Getters and setters

  public CfgProblemTimeProcessEntity toEntity() {
    try {
      CfgProblemTimeProcessEntity model = new CfgProblemTimeProcessEntity(
          StringUtils.validString(id) ? Long.valueOf(id) : null,
          cfgCode,
          typeCode,
          priorityCode,
          StringUtils.validString(rcaFoundTime) ? Double.parseDouble(rcaFoundTime) : null,
          StringUtils.validString(waFoundTime) ? Double.parseDouble(waFoundTime) : null,
          StringUtils.validString(slFoundTime) ? Double.parseDouble(slFoundTime) : null,
          lastUpdateTime
      );
      return model;
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      return null;

    }
  }
}
