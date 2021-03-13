package com.viettel.gnoc.risk.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.validator.Unique;
import com.viettel.gnoc.risk.model.RiskTypeEntity;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Unique(message = "{validation.riskTypeDTO.multiple.unique}", clazz = RiskTypeEntity.class,
    uniqueField = "riskTypeCode", idField = "riskTypeId")
public class RiskTypeDTO extends BaseDto {

  private Long riskTypeId;
  @NotNull(message = "validation.riskTypeDTO.riskTypeCode.notNull")
  @Size(max = 500, message = "validation.riskTypeDTO.riskTypeCode.tooLong")
  private String riskTypeCode;
  @NotNull(message = "validation.riskTypeDTO.riskTypeName.notNull")
  @Size(max = 500, message = "validation.riskTypeDTO.riskTypeName.tooLong")
  private String riskTypeName;
  @NotNull(message = "validation.riskTypeDTO.status.notNull")
  private Long status;
  private Long riskGroupTypeId;

  private Long priorityId;
  private String statusName;
  private List<RiskTypeDetailDTO> listRiskTypeDetailDTO;
  private String riskGroupTypeName;
  private String priorityName;
  private Double processTime;
  private Long warningSchedule;
  private Long timeWarningOverdue;
  private String processTimeStr;
  private String warningScheduleStr;
  private String timeWarningOverdueStr;
  private Long action;
  private String actionName;
  private String resultImport;

  public RiskTypeDTO(Long riskTypeId, String riskTypeCode, String riskTypeName, Long status,
      Long riskGroupTypeId) {
    this.riskTypeId = riskTypeId;
    this.riskTypeCode = riskTypeCode;
    this.riskTypeName = riskTypeName;
    this.status = status;
    this.riskGroupTypeId = riskGroupTypeId;
  }

  public RiskTypeEntity toEntity() {
    return new RiskTypeEntity(riskTypeId, riskTypeCode, riskTypeName, status, riskGroupTypeId);
  }

}
