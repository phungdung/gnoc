package com.viettel.gnoc.pt.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.pt.model.ProblemCrEntity;
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
public class ProblemCrDTO extends BaseDto {

  //Fields
  private Long problemCrId;
  private Long problemId;
  private Long crId;
  private Long ptStatusId;

  private String crNumber;
  private String crName;
  private String state;
  private String createPersonName;
  private String startTime;
  private String endTime;

  public ProblemCrDTO(Long problemCrId, Long problemId, Long crId, Long ptStatusId) {
    this.problemCrId = problemCrId;
    this.problemId = problemId;
    this.crId = crId;
    this.ptStatusId = ptStatusId;
  }

  public ProblemCrEntity toEntity() {
    ProblemCrEntity model = new ProblemCrEntity(
        problemCrId,
        problemId,
        crId,
        ptStatusId);
    return model;
  }
}
