package com.viettel.gnoc.pt.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.pt.model.ProblemWoEntity;
import javax.validation.constraints.NotNull;
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
public class ProblemWoDTO extends BaseDto {

  private Long problemWoId;
  @NotNull(message = "{validation.problemWoDTO.problemId.NotNull}")
  private Long problemId;
  @NotNull(message = "{validation.problemWoDTO.woId.NotNull}")
  private Long woId;
  @NotNull(message = "{validation.problemWoDTO.ptStatusId.NotNull}")
  private Long ptStatusId;

  public ProblemWoEntity toEntity() {
    ProblemWoEntity model = new ProblemWoEntity(
        problemWoId, problemId, woId, ptStatusId);
    return model;
  }

}
