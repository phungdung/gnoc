package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.wo.model.WoTypeCheckListEntity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WoTypeCheckListDTO extends BaseDto {

  //Fields
  private Long woTypeChecklistId;
  private Long woTypeId;

  @NotEmpty(message = "{validation.woTypeCheckList.null.checklistName}")
  @Size(max = 256, message = "{validation.woTypeCheckList.checklistName.tooLong}")
  private String checklistName;

  @Size(max = 1024, message = "{validation.woTypeCheckList.defaultValue.tooLong}")
  private String defaultValue;
  private Long isEnable;

  private Long woId;
  private Long woChecklistDetailId;
  private String checklistValue;


  public WoTypeCheckListDTO(Long woTypeChecklistId, Long woTypeId, String checklistName,
      String defaultValue, Long isEnable) {
    this.woTypeChecklistId = woTypeChecklistId;
    this.woTypeId = woTypeId;
    this.checklistName = checklistName;
    this.defaultValue = defaultValue;
    this.isEnable = isEnable;
  }

  public WoTypeCheckListEntity toEntity() {
    WoTypeCheckListEntity model = new WoTypeCheckListEntity(
        woTypeChecklistId
        , woTypeId
        , checklistName
        , defaultValue
        , isEnable
    );
    return model;
  }
}
