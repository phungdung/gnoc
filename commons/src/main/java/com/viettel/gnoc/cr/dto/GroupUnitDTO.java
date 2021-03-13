package com.viettel.gnoc.cr.dto;


import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.validator.Unique;
import com.viettel.gnoc.cr.model.GroupUnitEntity;
import java.util.List;
import javax.validation.constraints.NotEmpty;
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
@Unique(message = "{validation.groupUnitDTO.groupUnitCode.unique}", clazz = GroupUnitEntity.class, uniqueField = "groupUnitCode", idField = "groupUnitId")
public class GroupUnitDTO extends BaseDto {

  private Long groupUnitId;
  @NotEmpty(message = "{validation.groupUnitDTO.groupUnitCode.NotEmpty}")
  private String groupUnitCode;
  @NotEmpty(message = "{validation.groupUnitDTO.groupUnitName.NotEmpty}")
  private String groupUnitName;
  private Long isActive;
  private List<LanguageExchangeDTO> listGroupUnitName;

  public GroupUnitDTO(Long groupUnitId, String groupUnitCode, String groupUnitName, Long isActive) {
    this.groupUnitId = groupUnitId;
    this.groupUnitCode = groupUnitCode;
    this.groupUnitName = groupUnitName;
    this.isActive = isActive;
  }

  public GroupUnitEntity toEntity() {
    return new GroupUnitEntity(groupUnitId, groupUnitCode, groupUnitName,
        isActive);
  }
}
