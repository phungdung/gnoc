package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.wo.model.UserGroupCategoryEntity;
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

  private Long ugcyId;
  private String ugcyCode;
  private String ugcyName;
  private Long ugcySystem;
  private Long ugcyIsActive;

  private String defaultSortField;

  public UserGroupCategoryDTO(Long ugcyId, String ugcyCode, String ugcyName,
      Long ugcySystem, Long ugcyIsActive) {
    this.ugcyId = ugcyId;
    this.ugcyCode = ugcyCode;
    this.ugcyName = ugcyName;
    this.ugcySystem = ugcySystem;
    this.ugcyIsActive = ugcyIsActive;
  }

  public UserGroupCategoryEntity toEntity() {
    return new UserGroupCategoryEntity(ugcyId,
        ugcyCode,
        ugcyName,
        ugcySystem,
        ugcyIsActive);
  }

}
