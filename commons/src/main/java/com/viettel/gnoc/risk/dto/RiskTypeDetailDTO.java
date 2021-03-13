package com.viettel.gnoc.risk.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.risk.model.RiskTypeDetailEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RiskTypeDetailDTO extends BaseDto {

  private Long id;
  private Long riskTypeId;
  private Long priorityId;
  private Double processTime;
  private Long timeWarningOverdue;
  private Long warningSchedule;

  public RiskTypeDetailEntity toEntity() {
    return new RiskTypeDetailEntity(id, riskTypeId, priorityId, processTime, timeWarningOverdue,
        warningSchedule);
  }

}
