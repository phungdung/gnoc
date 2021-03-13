package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.validator.Unique;
import com.viettel.gnoc.cr.model.ImpactSegmentEntity;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author DungPV
 */

@Getter
@Setter
@NoArgsConstructor
@Unique(message = "{validation.ImpactSegmentDTO.impactSegmentCode.unique}", clazz = ImpactSegmentEntity.class, uniqueField = "impactSegmentCode", idField = "impactSegmentId")
public class ImpactSegmentDTO extends BaseDto {

  private Long impactSegmentId;
  @NotEmpty(message = "{validation.ImpactSegmentDTO.impactSegmentCode.NotEmpty}")
  @Size(max = 100, message = "{validation.ImpactSegmentDTO.impactSegmentCode.tooLong}")
  private String impactSegmentCode;
  @NotEmpty(message = "{validation.ImpactSegmentDTO.impactSegmentName.NotEmpty}")
  @Size(max = 200, message = "{validation.ImpactSegmentDTO.impactSegmentName.tooLong}")
  private String impactSegmentName;
  @NotNull(message = "{validation.ImpactSegmentDTO.appliedSystem.NotNull}")
  private Long appliedSystem;
  private String appliedSystemName;
  private Long isActive;
  private List<LanguageExchangeDTO> listImpactSegmentName;

  public ImpactSegmentDTO(Long impactSegmentId, String impactSegmentCode, String impactSegmentName,
      Long appliedSystem, Long isActive) {
    this.impactSegmentId = impactSegmentId;
    this.impactSegmentCode = impactSegmentCode;
    this.impactSegmentName = impactSegmentName;
    this.appliedSystem = appliedSystem;
    this.isActive = isActive;
  }

  public ImpactSegmentEntity toEntity() {
    return new ImpactSegmentEntity(impactSegmentId, impactSegmentCode, impactSegmentName,
        appliedSystem, isActive);
  }
}
