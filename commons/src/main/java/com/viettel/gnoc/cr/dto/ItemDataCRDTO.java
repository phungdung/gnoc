package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
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
public class ItemDataCRDTO extends BaseDto {

  private String defaultSortField;
  private String valueStr = "";
  private String secondValue = "";
  private String displayStr = "";
  private String thirdValue = "";
}
