package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.validator.SizeByte;
import com.viettel.gnoc.commons.validator.Unique;
import com.viettel.gnoc.wo.model.WoCdGroupTypeEntity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Unique(message = "{validation.woCdGroupTypeDTO.multiple.unique}", clazz = WoCdGroupTypeEntity.class,
    uniqueField = "groupTypeCode", idField = "groupTypeId")
public class WoCdGroupTypeDTO extends BaseDto {

  @NotNull(message = "validation.woCdGroupTypeDTO.groupTypeId.NotNull")
  private Long groupTypeId;

  @NotNull(message = "validation.woCdGroupTypeDTO.groupTypeName.NotNull")
  @Size(max = 100, message = "validation.woCdGroupTypeDTO.groupTypeName.tooLong")
  private String groupTypeName;

  @NotNull(message = "validation.woCdGroupTypeDTO.groupTypeCode.NotNull")
  @SizeByte(max = 50, message = "validation.woCdGroupTypeDTO.groupTypeCode.tooLong")
  private String groupTypeCode;

  private Long isEnable;

  public WoCdGroupTypeDTO(
      Long groupTypeId,
      String groupTypeCode,
      String groupTypeName,
      Long isEnable) {
    this.groupTypeId = groupTypeId;
    this.groupTypeCode = groupTypeCode;
    this.groupTypeName = groupTypeName;
    this.isEnable = isEnable;
  }

  public WoCdGroupTypeEntity toEntity() {
    return new WoCdGroupTypeEntity(groupTypeId, groupTypeCode, groupTypeName, isEnable);
  }
}
