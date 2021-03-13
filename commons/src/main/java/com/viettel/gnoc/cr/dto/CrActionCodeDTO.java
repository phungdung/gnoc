package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.validator.Unique;
import com.viettel.gnoc.cr.model.CrActionCodeEntity;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Unique(message = "{validation.cr.crActionCode.isDupplicate.CR}", clazz = CrActionCodeEntity.class, uniqueField = "crActionCode", idField = "crActionCodeId")
public class CrActionCodeDTO extends BaseDto {

  private Long crActionCodeId;

  @NotEmpty(message = "{validation.actionCode.null.crActionCode}")
  private String crActionCode;

  @NotEmpty(message = "{validation.actionCode.null.crActionCodeTittle}")
  private String crActionCodeTittle;
  private Long isActive;
  private Long isEditable;

  public CrActionCodeEntity toEntity() {
    return new CrActionCodeEntity(
        crActionCodeId,
        crActionCode,
        crActionCodeTittle,
        isActive,
        isEditable
    );
  }
}
