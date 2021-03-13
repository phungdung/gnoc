package com.viettel.gnoc.pt.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.pt.model.ProblemWorklogEntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ProblemWorklogDTO extends BaseDto {

  private Long problemWorklogId;
  private Long createUserId;
  private String createUserName;
  private Long createUnitId;
  private String createUnitName;
  private String worklog;
  private String description;
  private Date createdTime;
  private Long problemId;

  public ProblemWorklogEntity toEntity() {
    ProblemWorklogEntity model = new ProblemWorklogEntity(
        problemWorklogId,
        createUserId,
        createUserName,
        createUnitId,
        createUnitName,
        worklog,
        description,
        createdTime,
        problemId
    );
    return model;
  }
}
