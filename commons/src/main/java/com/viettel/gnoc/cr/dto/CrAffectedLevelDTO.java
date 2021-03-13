package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.validator.Unique;
import com.viettel.gnoc.cr.model.CrAffectedLevelEntity;
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
@Setter
@Getter
@NoArgsConstructor
@Unique(message = "{validation.CrAffectedLevelDTO.affectedLevelCode.unique}", clazz = CrAffectedLevelEntity.class, uniqueField = "affectedLevelCode", idField = "affectedLevelId")
public class CrAffectedLevelDTO extends BaseDto {

  private Long affectedLevelId;
  @NotEmpty(message = "{validation.CrAffectedLevelDTO.affectedLevelCode.NotEmpty}")
  @Size(max = 100, message = "{validation.CrAffectedLevelDTO.affectedLevelCode.tooLong}")
  private String affectedLevelCode;
  @NotEmpty(message = "{validation.CrAffectedLevelDTO.affectedLevelName.NotEmpty}")
  @Size(max = 200, message = "{validation.CrAffectedLevelDTO.affectedLevelName.tooLong}")
  private String affectedLevelName;
  @NotNull(message = "{validation.CrAffectedLevelDTO.twoApproveLevel.NotNull}")
  private Long twoApproveLevel;
  private String twoApproveLevelName;
  @NotNull(message = "{validation.CrAffectedLevelDTO.appliedSystem.NotNull}")
  private Long appliedSystem;
  private String appliedSystemName;
  private Long isActive;
  private List<LanguageExchangeDTO> listAffectedLevelName;

  public CrAffectedLevelDTO(Long affectedLevelId, String affectedLevelCode,
      String affectedLevelName, Long twoApproveLevel, Long appliedSystem, Long isActive) {
    this.affectedLevelId = affectedLevelId;
    this.affectedLevelCode = affectedLevelCode;
    this.affectedLevelName = affectedLevelName;
    this.twoApproveLevel = twoApproveLevel;
    this.appliedSystem = appliedSystem;
    this.isActive = isActive;
  }

  public CrAffectedLevelEntity toEntity() {
    return new CrAffectedLevelEntity(affectedLevelId, affectedLevelCode, affectedLevelName,
        twoApproveLevel, appliedSystem, isActive);
  }
}
