package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.utils.StringUtils;
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
public class WorkLogCategoryDTO {

  private String wlayId;
  private String wlayType;
  private String wlayCode;
  private String wlayName;
  private String wlayDescription;
  private String wlayIsActive;
  private String wlayIsEditable;
  private String defaultSortField;

  public WorkLogCategoryInsideDTO toInsideDTO() {
    return new WorkLogCategoryInsideDTO(
        StringUtils.isStringNullOrEmpty(wlayId) ? null : Long.parseLong(wlayId),
        StringUtils.isStringNullOrEmpty(wlayType) ? null : Long.parseLong(wlayType),
        wlayCode,
        wlayName,
        wlayDescription,
        StringUtils.isStringNullOrEmpty(wlayIsActive) ? null : Long.parseLong(wlayIsActive),
        StringUtils.isStringNullOrEmpty(wlayIsEditable) ? null : Long.parseLong(wlayIsEditable)
    );
  }
}
