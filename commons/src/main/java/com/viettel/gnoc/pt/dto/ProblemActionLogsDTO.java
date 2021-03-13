/**
 * @(#)ProblemActionLogsForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */

package com.viettel.gnoc.pt.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.validator.SizeByte;
import com.viettel.gnoc.pt.model.ProblemActionLogsEntity;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProblemActionLogsDTO extends BaseDto {

  //Fields
  private Long problemActionLogsId;

  @SizeByte(max = 4000, message = "{validation.problem.actionLog.content.InvalidMaxLength}")
  private String content;
  private Date createTime;
  private Long createUnitId;
  private Long createUserId;

  @SizeByte(max = 1000, message = "{validation.problem.actionLog.type.InvalidMaxLength}")
  private String type;

  @NotNull(message = "{validation.problem.actionLog.problemIdRequired}")
  private Long problemId;

  @SizeByte(max = 1000, message = "{validation.problem.actionLog.unitName.InvalidMaxLength}")
  private String createrUnitName;

  @SizeByte(max = 1000, message = "{validation.problem.actionLog.userName.InvalidMaxLength}")
  private String createrUserName;

  private String note;

  public ProblemActionLogsEntity toEntity() {
    ProblemActionLogsEntity model = new ProblemActionLogsEntity(
        problemActionLogsId,
        content,
        createTime,
        createUnitId,
        createUserId,
        type,
        problemId,
        createrUnitName,
        createrUserName, note);
    return model;
  }
}

