package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.validator.Unique;
import com.viettel.gnoc.cr.model.WorkLogCategoryEntity;
import java.util.List;
import javax.validation.constraints.NotNull;
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
@Unique(message = "{validation.cr.workLogCategory.isDupplicate.CR}", clazz = WorkLogCategoryEntity.class, uniqueField = "wlayCode", idField = "wlayId")

public class WorkLogCategoryInsideDTO extends BaseDto {

  private Long wlayId;
  @NotNull(message = "{validation.worklogCategory.null.wlayType}")
  private Long wlayType;
  @NotNull(message = "{validation.worklogCategory.null.wlayCode}")
  private String wlayCode;
  private String wlayName;
  private String wlayNameType;
  private String wlayDescription;
  private Long wlayIsActive;
  private Long wlayIsEditable;
  private List<LanguageExchangeDTO> listWorkLogCategory;
  private String defaultSortField;

  public WorkLogCategoryInsideDTO(Long wlayId, Long wlayType, String wlayCode, String wlayName,
      String wlayDescription, Long wlayIsActive, Long wlayIsEditable) {
    this.wlayId = wlayId;
    this.wlayType = wlayType;
    this.wlayCode = wlayCode;
    this.wlayName = wlayName;
    this.wlayDescription = wlayDescription;
    this.wlayIsActive = wlayIsActive;
    this.wlayIsEditable = wlayIsEditable;
  }

  public WorkLogCategoryDTO toOutSide() {
    return new WorkLogCategoryDTO(
        StringUtils.isStringNullOrEmpty(wlayId) ? null : String.valueOf(wlayId),
        StringUtils.isStringNullOrEmpty(wlayType) ? null : String.valueOf(wlayType),
        wlayCode,
        wlayName,
        wlayDescription,
        StringUtils.isStringNullOrEmpty(wlayIsActive) ? null : String.valueOf(wlayIsActive),
        StringUtils.isStringNullOrEmpty(wlayIsEditable) ? null : String.valueOf(wlayIsEditable),
        null
    );
  }

  public WorkLogCategoryEntity toEntity() {
    return new WorkLogCategoryEntity(
        wlayId,
        wlayType,
        wlayCode,
        wlayName,
        wlayDescription,
        wlayIsActive,
        wlayIsEditable
    );
  }
}
