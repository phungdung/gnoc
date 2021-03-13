package com.viettel.gnoc.ws.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
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
public class UserGroupCategoryDTO extends BaseDto {

  private String ugcyId;
  private String ugcyCode;
  private String ugcyName;
  private String ugcySystem;
  private String ugcyIsActive;

  private String defaultSortField;

  public UserGroupCategoryDTO(String ugcyId, String ugcyCode, String ugcyName, String ugcySystem,
      String ugcyIsActive) {
    this.ugcyId = ugcyId;
    this.ugcyCode = ugcyCode;
    this.ugcyName = ugcyName;
    this.ugcySystem = ugcySystem;
    this.ugcyIsActive = ugcyIsActive;
  }

}
