package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import com.viettel.gnoc.cr.model.GroupUnitDetailEntity;
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
@MultiFieldUnique(message = "{validation.groupUnitDetailDTO.isDupplicate.CR}", clazz = GroupUnitDetailEntity.class,
    uniqueFields = "groupUnitId,unitId", idField = "groupUnitDetailId")
public class GroupUnitDetailDTO extends BaseDto {

  private Long groupUnitDetailId;
  @NotNull(message = "{validation.groupUnitDetailDTO.groupUnitId.NotNull}")
  private Long groupUnitId;
  @NotNull(message = "{validation.groupUnitDetailDTO.unitId.NotNull}")
  private Long unitId;

  public GroupUnitDetailEntity toEntity() {
    return new GroupUnitDetailEntity(groupUnitDetailId, groupUnitId, unitId);
  }
}
